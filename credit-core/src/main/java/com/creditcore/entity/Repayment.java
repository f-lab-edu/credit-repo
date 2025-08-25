package com.creditcore.entity;

import com.creditcore.enums.repayment.RepaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "repayments")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Repayment {

    @Id
    @Column(name = "repayment_id", nullable = false, unique = true)
    private String id;

    @Column(name = "principal", nullable = false)
    private BigDecimal principal;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status;

    @Column(name = "virtual_account")
    private String virtualAccount;

    @Column(name = "bank")
    private String bank;

    @Column(name = "transaction_key")
    private String transactionKey;

    public static Repayment create(BigDecimal principal, LocalDate dueDate, Contract contract, String orderId) {
        return Repayment.builder()
                .id(UUID.randomUUID().toString())
                .principal(principal)
                .dueDate(dueDate)
                .contract(contract)
                .status(RepaymentStatus.PENDING)
                .orderId(orderId)
                .build();
    }

    public void updateVirtualAccount(String accountNumber, String bank) {
        this.virtualAccount = accountNumber;
        this.bank = bank;
    }

    // 상환 완료 처리
    public void repayIsDone(String transactionKey) {
        this.status = RepaymentStatus.DONE;
        this.transactionKey = transactionKey;
    }

    // 기한 초과 처리
    public void repayIsOverDue() {
        this.status = RepaymentStatus.OVERDUE;
    }
}
