package com.credit.entity;

import com.credit.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @Column(name = "contract_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id", nullable = false)
    private Member lender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private Member borrower;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate repaymentDueDate;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Contract create(Member lender, Member borrower, BigDecimal amount, LocalDate repaymentDueDate) {
        LocalDateTime now = LocalDateTime.now();
        return Contract.builder()
                .id(UUID.randomUUID().toString())
                .lender(lender)
                .borrower(borrower)
                .amount(amount)
                .repaymentDueDate(repaymentDueDate)
                .orderId(UUID.randomUUID().toString())
                .status(ContractStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateStatus(ContractStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}