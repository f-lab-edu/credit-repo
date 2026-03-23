package com.credit.entity;

import com.credit.enums.RepaymentScheduleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "repayment_schedules")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepaymentSchedule {

    @Id
    @Column(name = "schedule_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepaymentScheduleStatus status;

    // 가상계좌 정보 (채권자가 계약을 확정하면 채워짐)
    @Column(name = "virtual_account_number")
    private String virtualAccountNumber;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "account_due_date")
    private String accountDueDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static RepaymentSchedule create(Contract contract) {
        LocalDateTime now = LocalDateTime.now();
        return RepaymentSchedule.builder()
                .id(UUID.randomUUID().toString())
                .contract(contract)
                .dueDate(contract.getRepaymentDueDate())
                .status(RepaymentScheduleStatus.PLANNED)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void assignVirtualAccount(String accountNumber, String bankCode,
                                     String accountHolderName, String accountDueDate) {
        this.virtualAccountNumber = accountNumber;
        this.bankCode = bankCode;
        this.accountHolderName = accountHolderName;
        this.accountDueDate = accountDueDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(RepaymentScheduleStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isVirtualAccountIssued() {
        return this.virtualAccountNumber != null;
    }
}