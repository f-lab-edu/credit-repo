package com.creditcore.service.contract;

import com.creditcore.dto.request.contract.ContractCreateRequest;
import com.creditcore.dto.response.contract.ContractAgreeResponse;
import com.creditcore.dto.response.contract.ContractCreateResponse;
import com.creditcore.dto.response.contract.ContractDetailResponse;
import com.creditcore.entity.User;
import com.creditcore.enums.contract.ContractStatus;
import com.creditcore.entity.Contract;
import com.creditcore.repository.contract.ContractRepository;
import com.creditcore.repository.user.UserRepository;
import com.creditexternalapi.toss.TossPaymentsClient;
import com.creditexternalapi.toss.dto.request.VirtualAccountRequest;
import com.creditexternalapi.toss.dto.response.VirtualAccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final TossPaymentsClient tossPaymentsClient;

    @Transactional
    public ContractCreateResponse createContract(ContractCreateRequest request){

        User lender = userRepository.findById(request.getLenderId())
                .orElseThrow(() -> new IllegalArgumentException("채권자를 찾을 수 없습니다."));
        User borrower = userRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new IllegalArgumentException("채무자를 찾을 수 없습니다."));


        Optional<Contract> checkDuplicateContract = contractRepository.findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                request.getBorrowerPhoneNumber(),
                request.getPrincipal(),
                request.getRepaymentDate(),
                ContractStatus.getActiveStatuses()
        );

        if (checkDuplicateContract.isPresent()) {
            throw new IllegalArgumentException("중복된 계약이 존재합니다.");
        }

        Contract contract = Contract.create(lender.getId(), borrower.getId(), request);
        Contract saved = contractRepository.save(contract);

        VirtualAccountRequest vaRequest = VirtualAccountRequest.ofBorrower(borrower.getName(), saved.getId(), request.getPrincipal().intValue());
        VirtualAccountResponse vaResponse = tossPaymentsClient.createVirtualAccount(vaRequest);

        return ContractCreateResponse.of(saved, vaResponse);
    }

    @Transactional(readOnly = true)
    public ContractDetailResponse getContractDetail(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다"));

        return ContractDetailResponse.of(contract);
    }

    @Transactional
    public ContractAgreeResponse agreeContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.PENDING_AGREEMENT) {
            throw new IllegalArgumentException("현재 계약 상태(" + contract.getStatus() + ")에서는 동의할 수 없습니다");
        }

        contract.updateStatus(ContractStatus.ACTIVE);
        Contract saved = contractRepository.save(contract);

        return ContractAgreeResponse.of(saved);
    }

    @Transactional
    public void cancelContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.PENDING_AGREEMENT &&
                contract.getStatus() != ContractStatus.ACTIVE) {
            throw new IllegalArgumentException("해당 상태(" + contract.getStatus() + ")에서는 취소할 수 없습니다.");
        }

        contract.updateStatus(ContractStatus.CANCELED);
        contractRepository.save(contract);
    }

    @Transactional
    public void completeContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.ACTIVE &&
                contract.getStatus() != ContractStatus.RECOVERY_IN_PROGRESS) {
            throw new IllegalArgumentException("해당 상태(" + contract.getStatus() + ")에서는 완료할 수 없습니다.");
        }

        contract.updateStatus(ContractStatus.COMPLETED);
        contractRepository.save(contract);
    }
}
