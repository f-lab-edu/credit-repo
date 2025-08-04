package com.creditcore.entity;

import com.credit.common.contract.RepaymentCycle;
import com.credit.common.recover.RecoverProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecoveryProgram {

    private String id; // pk
    private String contractId; // FK

    private Integer repaymentCount;
    private RepaymentCycle repaymentCycle;

    private RecoverProgramStatus status;

    private LocalDateTime proposedAt;
    private LocalDateTime approvedAt;
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
