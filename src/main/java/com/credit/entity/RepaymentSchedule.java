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

    public void updateStatus(RepaymentScheduleStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}