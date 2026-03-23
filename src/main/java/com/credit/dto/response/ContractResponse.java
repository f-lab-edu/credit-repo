package com.credit.dto.response;

import com.credit.entity.Contract;
import com.credit.enums.ContractStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ContractResponse {
    private String contractId;
    private String orderId;
    private String lenderName;
    private String borrowerName;
    private BigDecimal amount;
    private LocalDate repaymentDueDate;
    private ContractStatus status;
    private LocalDateTime createdAt;

    public static ContractResponse from(Contract contract) {
        return ContractResponse.builder()
                .contractId(contract.getId())
                .orderId(contract.getOrderId())
                .lenderName(contract.getLender().getName())
                .borrowerName(contract.getBorrower().getName())
                .amount(contract.getAmount())
                .repaymentDueDate(contract.getRepaymentDueDate())
                .status(contract.getStatus())
                .createdAt(contract.getCreatedAt())
                .build();
    }
}