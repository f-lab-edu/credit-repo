package com.creditcore.entity;

import com.creditcore.dto.request.contract.ContractCreateRequest;
import com.creditcore.enums.contract.ContractStatus;
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
@Table(name = "contract")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Contract {

    @Id
    @Column(name = "contract_id", nullable = false, unique = true)
    private String id;

    @Column(name = "lender_id", nullable = false)
    private String lenderId;

    @Column(name = "borrower_id", nullable = false)
    private String borrowerId;

    @Column(name = "borrower_phone_number", nullable = false)
    private String borrowerPhoneNumber;

    @Column(name = "principal", nullable = false)
    private BigDecimal principal;

    @Column(name = "repayment_date", nullable = false)
    private LocalDate repaymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status", nullable = false)
    private ContractStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 정적 빌더 패턴
    public static Contract create(String lenderId, String borrowerId, ContractCreateRequest request) {
        return Contract.builder()
                .id(UUID.randomUUID().toString())
                .lenderId(lenderId)
                .borrowerId(borrowerId)
                .borrowerPhoneNumber(request.getBorrowerPhoneNumber())
                .principal(request.getPrincipal())
                .repaymentDate(request.getRepaymentDate())
                .status(ContractStatus.PENDING_AGREEMENT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 상태 변경 메서드
    public void updateStatus(ContractStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
