package com.creditcore.service.webhook;

import com.creditcore.entity.Contract;
import com.creditcore.entity.Repayment;
import com.creditcore.enums.contract.ContractStatus;
import com.creditcore.enums.repayment.RepaymentStatus;
import com.creditcore.repository.contract.ContractRepository;
import com.creditcore.repository.repayment.RepaymentRepository;
import com.creditcore.service.payout.MockPayoutService;
import com.creditexternalapi.toss.dto.request.TossWebhookPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final ContractRepository contractRepository;
    private final RepaymentRepository repaymentRepository;
    private final MockPayoutService mockPayoutService;

    @Transactional
    public void handleVirtualAccountEvent(TossWebhookPayload payload) {
        if (!"DONE".equalsIgnoreCase(payload.getStatus())) {
            //TODO : 다른 상태는 일단 무시. 확장 필요
            return;
        }

        String orderId = payload.getOrderId();
        String txKey = payload.getTransactionKey();

        if (orderId.contains("-repay")) {
            // 상환 입금
            handleRepaymentDeposit(orderId, txKey);
        } else {
            // 대출 입금
            handleLoanDeposit(orderId);
        }
    }

    private void handleLoanDeposit(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));

        if (contract.getStatus() == ContractStatus.ACTIVE
                || contract.getStatus() == ContractStatus.RECOVERY_IN_PROGRESS
                || contract.getStatus() == ContractStatus.COMPLETED) {
            return;
        }

        if (contract.getStatus() != ContractStatus.PENDING_AGREEMENT) {
            throw new IllegalArgumentException("대출 실행 입금을 처리할 수 없는 상태 입니다. " + contract.getStatus());
        }

        contract.updateStatus(ContractStatus.ACTIVE);
        contractRepository.save(contract);

        String repayOrderId = contract.getId() + "-repay";

        if (repaymentRepository.existsByOrderId(repayOrderId)) {
            return;
        }

        Repayment repayment = Repayment.create(
                contract.getPrincipal(),
                contract.getRepaymentDate(),
                contract,
                repayOrderId
        );
        repaymentRepository.save(repayment);

        mockPayoutService.payoutToBorrowerOnLoanDeposit(contract);
    }

    private void handleRepaymentDeposit(String repaymentOrderId, String txKey) {
        Repayment repayment = repaymentRepository.findByOrderId(repaymentOrderId)
                .orElseThrow(() -> new IllegalArgumentException("상환 건을 찾을 수 없습니다. orderId=" + repaymentOrderId));

        // 멱등 처리
        if (repayment.getStatus() == RepaymentStatus.DONE) {
            return;
        }

        repayment.repayIsDone(txKey);
        repaymentRepository.save(repayment);

        Contract contract = repayment.getContract();
        if (contract.getStatus() != ContractStatus.COMPLETED) {
            contract.updateStatus(ContractStatus.COMPLETED);
            contractRepository.save(contract);
        }

        mockPayoutService.payoutToLenderOnRepayment(contract);
    }
}
