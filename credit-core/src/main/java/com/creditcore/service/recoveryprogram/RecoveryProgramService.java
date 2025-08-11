package com.creditcore.service.recoveryprogram;

import com.creditcore.dto.request.recovery.RecoveryProgramCreateRequest;
import com.creditcore.dto.response.recovery.RecoveryProgramAgreeResponse;
import com.creditcore.dto.response.recovery.RecoveryProgramCreateResponse;
import com.creditcore.enums.contract.ContractStatus;
import com.creditcore.enums.recover.RecoverProgramStatus;
import com.creditcore.entity.Contract;
import com.creditcore.entity.RecoveryProgram;
import com.creditcore.repository.contract.ContractRepository;
import com.creditcore.repository.recovery.RecoveryProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecoveryProgramService {

    private final RecoveryProgramRepository recoveryProgramRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public RecoveryProgramCreateResponse createRecoveryProgram(String contractId, RecoveryProgramCreateRequest request) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.FAILED) {
            throw new IllegalArgumentException("회복 프로그램ㅇ 제안은 채무 불이행 상태에서만 가능합니다.");
        }

        RecoveryProgram newRecoveryProgram = RecoveryProgram.create(contractId, request.getRepaymentCount(), request.getRepaymentCycle());
        recoveryProgramRepository.save(newRecoveryProgram);

        contract.updateStatus(ContractStatus.RECOVERY_PENDING_AGREEMENT);
        contractRepository.save(contract);

        return RecoveryProgramCreateResponse.from(contract.getId(), contract.getStatus());
    }

    @Transactional
    public RecoveryProgramAgreeResponse agreeRecoveryProgram(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.RECOVERY_PENDING_AGREEMENT) {
            throw new IllegalArgumentException("현재 계약 상태(" + contract.getStatus() + ")에서는 신뢰 회복 프로그램에 동의할 수 없습니다.");
        }

        RecoveryProgram recoveryProgram = recoveryProgramRepository.findByContractIdAndStatus(contractId, RecoverProgramStatus.PENDING)
                .orElseThrow(() -> new IllegalArgumentException("해당 계약에 대해 동의 대기 중인 신뢰 회복 프로그램을 찾을 수 없습니다."));

        recoveryProgram.updateStatus(RecoverProgramStatus.IN_PROGRESS);
        recoveryProgramRepository.save(recoveryProgram);

        contract.updateStatus(ContractStatus.RECOVERY_IN_PROGRESS);
        contractRepository.save(contract);

        return RecoveryProgramAgreeResponse.from(contractId, contract.getStatus());
    }
}
