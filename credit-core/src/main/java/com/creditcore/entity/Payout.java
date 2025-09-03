package com.creditcore.entity;

import com.creditcore.enums.payout.PayoutStatus;
import com.creditcore.enums.payout.PayoutType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payouts")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payout {

    @Id
    @Column(name = "payout_id", nullable = false, unique = true)
    private String id;

    @Column(name = "contract_id", nullable = false)
    private String contractId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payout_type", nullable = false)
    private PayoutType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "target_user_id", nullable = false)
    private String targetUserId;

    @Column(name = "target_bank_code", nullable = false)
    private String targetBankCode;

    @Column(name = "target_account_number", nullable = false)
    private String targetAccountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayoutStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "mock_transaction_id")
    private String mockTransactionId;

    public static Payout request(String contractId,
                                 PayoutType type,
                                 BigDecimal amount,
                                 String targetUserId,
                                 String bankCode,
                                 String accountNumber) {
        return Payout.builder()
                .id(UUID.randomUUID().toString())
                .contractId(contractId)
                .type(type)
                .amount(amount)
                .targetUserId(targetUserId)
                .targetBankCode(bankCode)
                .targetAccountNumber(accountNumber)
                .status(PayoutStatus.REQUESTED)
                .requestedAt(LocalDateTime.now())
                .build();
    }

    public void completed(String mockTransactionId) {
        this.status = PayoutStatus.COMPLETED;
        this.mockTransactionId = mockTransactionId;
        this.completedAt = LocalDateTime.now();
    }

    public void failed(String reason) {
        this.status = PayoutStatus.FAILED;
        this.mockTransactionId = reason;
    }
}
