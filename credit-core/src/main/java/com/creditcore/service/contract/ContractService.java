package com.creditcore.service.contract;

import com.credit.common.contract.ContractStatus;
import com.credit.common.contract.request.ContractCreateRequest;
import com.creditcore.entity.Contract;
import com.creditcore.repository.contract.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;

    @Transactional
    public Contract createContract(ContractCreateRequest request) {
        Optional<Contract> checkDuplicateContract = contractRepository.findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                request.getBorrowerPhoneNumber(),
                request.getPrincipal(),
                request.getRepaymentDate(),
                ContractStatus.getActiveStatuses()
        );

        if (checkDuplicateContract.isPresent()) {
            throw new IllegalArgumentException("중복된 계약이 존재합니다.");
        }

        Contract contract = Contract.create(request);

        // 추후 DTO 변환해서 반환, 현재까지는 엔티티 반환 유지
        return contractRepository.save(contract);
    }

    @Transactional(readOnly = true)
    public Contract getContractDetail(String contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(()-> new IllegalArgumentException("계약을 찾을 수 없습니다"));
    }

    @Transactional
    public Contract agreeContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.PENDING_AGREEMENT) {
            throw new IllegalArgumentException("현재 계약 상태(" + contract.getStatus() + ")에서는 동의할 수 없습니다");
        }

        contract.updateStatus(ContractStatus.ACTIVE);

        return contractRepository.save(contract);
    }
}
