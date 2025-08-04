package com.creditcore.entity;

import com.credit.common.contract.ContractStatus;
import com.credit.common.contract.request.ContractCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Contract {

    private String id;
    private String borrowerPhoneNumber;
    private BigDecimal principal;
    private LocalDate repaymentDate;
    private ContractStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 정적 빌더 패턴
    public static Contract create(ContractCreateRequest request) {
        return Contract.builder()
                .id(UUID.randomUUID().toString())
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
