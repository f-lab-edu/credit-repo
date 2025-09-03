package com.creditcore.dto.response.contract;

import com.creditcore.entity.Contract;
import com.creditcore.enums.contract.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDetailResponse {
    private String contractId;
    private String lenderName;
    private String borrowerName;
    private BigDecimal principal;
    private LocalDate repaymentDate;
    private ContractStatus status;
    private LocalDateTime createdAt;

    public static ContractDetailResponse of(Contract contract) {
        return ContractDetailResponse.builder()
                .contractId(contract.getId())
                .lenderName("채권자")   // TODO: User 연동 후 변경
                .borrowerName(contract.getBorrowerPhoneNumber())
                .principal(contract.getPrincipal())
                .repaymentDate(contract.getRepaymentDate())
                .status(contract.getStatus())
                .createdAt(contract.getCreatedAt())
                .build();
    }
}
