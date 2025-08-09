package com.api.dto.response;

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
}
