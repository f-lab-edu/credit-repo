package com.creditcore.entity;

import com.credit.common.contract.RepaymentCycle;
import com.credit.common.recover.RecoverProgramStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recovery_program")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecoveryProgram {

    @Id
    @Column(name = "program_id", nullable = false, unique = true)
    private String id; // pk

    @Column(name = "contract_id", nullable = false)
    private String contractId; // FK

    @Column(name = "repayment_count", nullable = false)
    private Integer repaymentCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "repayment_cycle", nullable = false)
    private RepaymentCycle repaymentCycle;

    @Enumerated(EnumType.STRING)
    @Column(name = "recovery_programe_status", nullable = false)
    private RecoverProgramStatus status;

    @Column(name = "proposed_at", nullable = false, updatable = false)
    private LocalDateTime proposedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    //정적 메서드
    public static RecoveryProgram create(String contractId, Integer repaymentCount, RepaymentCycle repaymentCycle) {
        return RecoveryProgram.builder()
                .id(java.util.UUID.randomUUID().toString())
                .contractId(contractId)
                .repaymentCount(repaymentCount)
                .repaymentCycle(repaymentCycle)
                .status(RecoverProgramStatus.PENDING) // 초기 상태
                .proposedAt(LocalDateTime.now())
                .build();
    }

    // 상태 변경 메서드
    public void updateStatus(RecoverProgramStatus newStatus) {
        this.status = newStatus;
        if (newStatus == RecoverProgramStatus.APPROVED) {
            this.approvedAt = LocalDateTime.now();
        } else if (newStatus == RecoverProgramStatus.COMPLETED ||
                newStatus == RecoverProgramStatus.REJECTED ||
                newStatus == RecoverProgramStatus.CANCELLED) {
            this.endedAt = LocalDateTime.now();
        }
    }
}
