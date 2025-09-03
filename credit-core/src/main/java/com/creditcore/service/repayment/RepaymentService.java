package com.creditcore.service.repayment;

import com.creditcore.entity.Contract;
import com.creditcore.entity.Repayment;
import com.creditcore.entity.User;
import com.creditcore.enums.contract.ContractStatus;
import com.creditcore.enums.repayment.RepaymentStatus;
import com.creditcore.repository.contract.ContractRepository;
import com.creditcore.repository.repayment.RepaymentRepository;
import com.creditcore.repository.user.UserRepository;
import com.creditexternalapi.toss.TossPaymentsClient;
import com.creditexternalapi.toss.dto.request.VirtualAccountRequest;
import com.creditexternalapi.toss.dto.response.VirtualAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepaymentService {
    private final ContractRepository contractRepository;
    private final RepaymentRepository repaymentRepository;
    private final UserRepository userRepository;
    private final TossPaymentsClient tossPaymentsClient;

    @Transactional
    public VirtualAccountResponse createRepaymentVirtualAccount(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        User lender = userRepository.findById(contract.getLenderId())
                .orElseThrow(() -> new IllegalArgumentException("채무자를 찾을 수 없습니다."));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new IllegalStateException("상환 가상계좌는 ACTIVE 상태에서만 발급 가능합니다.");
        }

        String repayOrderId = contractId + "-repay";
        Repayment repayment = repaymentRepository.findByOrderId(repayOrderId)
                .orElseThrow(() -> new IllegalArgumentException("상환 건을 찾을 수 없습니다. orderId=" + repayOrderId));

        if (repayment.getStatus() != RepaymentStatus.PENDING) {
            throw new IllegalStateException("상환 대기 상태가 아닙니다. 현재: " + repayment.getStatus());
        }

        VirtualAccountRequest request = VirtualAccountRequest.ofLender(
                lender.getName(),
                contract.getId(),
                contract.getPrincipal().intValue()
        );
        VirtualAccountResponse response = tossPaymentsClient.createVirtualAccount(request);

        repayment.updateVirtualAccount(
                response.getVirtualAccount().getAccountNumber(),
                response.getVirtualAccount().getBankCode()
        );

        repaymentRepository.save(repayment);

        // 응답 DTO
        return response;
    }
}
