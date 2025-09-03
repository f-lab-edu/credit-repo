package com.creditcore.service.payout;

import com.creditcore.entity.Contract;
import com.creditcore.entity.Payout;
import com.creditcore.entity.User;
import com.creditcore.enums.payout.PayoutType;
import com.creditcore.repository.payout.PayoutRepository;
import com.creditcore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockPayoutService {

    private final PayoutRepository payoutRepository;
    private final UserRepository userRepository;

    // 대출 실행 입금 확인 후 -> 채무자에게 지급
    @Transactional
    public void payoutToBorrowerOnLoanDeposit(Contract contract) {
        User borrower = userRepository.findById(contract.getBorrowerId())
                .orElseThrow(() -> new IllegalArgumentException("채무자를 찾을 수 없습니다."));

        Payout payout = Payout.request(
                contract.getId(),
                PayoutType.LOAN,
                contract.getPrincipal(),
                borrower.getId(),
                borrower.getBankCode(),
                borrower.getAccountNumber()
        );
        payoutRepository.save(payout);

        String mockTxId = "MOCK-" + UUID.randomUUID();
        payout.completed(mockTxId);
        payoutRepository.save(payout);

        log.info("[MOCK-PAYOUT] 대출 실행 지급 완료: contractId = {}, borrower = {}, bank = {}, account = {}, amount = {}, mockTxId = {}",
                contract.getId(), borrower.getName(), borrower.getBankCode(), borrower.getAccountNumber(), contract.getPrincipal(), payout.getMockTransactionId());
    }

    @Transactional
    public void payoutToLenderOnRepayment(Contract contract) {
        User lender = userRepository.findById(contract.getLenderId())
                .orElseThrow(() -> new IllegalArgumentException("채권자를 찾을 수 없습니다."));

        Payout payout = Payout.request(
                contract.getId(),
                PayoutType.REPAYMENT,
                contract.getPrincipal(),
                lender.getId(),
                lender.getBankCode(),
                lender.getAccountNumber()
        );
        payoutRepository.save(payout);

        String mockTxId = "MOCK-" + UUID.randomUUID();
        payout.completed(mockTxId);
        payoutRepository.save(payout);

        log.info("[MOCK-PAYOUT] 상환 지급 완료: contractId = {}, lender = {}, bank = {}, account = {}, amount = {}, mockTxID = {}",
                contract.getId(), lender.getName(), lender.getBankCode(), lender.getAccountNumber(), contract.getPrincipal(), payout.getMockTransactionId());
    }
}
