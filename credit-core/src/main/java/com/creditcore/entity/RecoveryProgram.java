package com.creditcore.entity;

import com.creditcore.enums.contract.RepaymentCycle;
import com.creditcore.enums.recover.RecoverProgramStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private LocalDateTime createdAt;


    //정적 메서드
    public static RecoveryProgram create(String contractId, Integer repaymentCount, RepaymentCycle repaymentCycle) {
        return RecoveryProgram.builder()
                .id(UUID.randomUUID().toString())
                .contractId(contractId)
                .repaymentCount(repaymentCount)
                .repaymentCycle(repaymentCycle)
                .status(RecoverProgramStatus.PENDING) // 초기 상태
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 상태 변경 메서드
    public void updateStatus(RecoverProgramStatus newStatus) {
        this.status = newStatus;
    }
}
