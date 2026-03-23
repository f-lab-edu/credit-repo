package com.credit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ContractCreateRequest {

    @NotBlank(message = "채권자 ID는 필수입니다.")
    private String lenderId;

    @NotBlank(message = "채무자 ID는 필수입니다.")
    private String borrowerId;

    @NotNull(message = "대여 금액은 필수입니다.")
    @Positive(message = "대여 금액은 0보다 커야 합니다.")
    private BigDecimal amount;

    @NotNull(message = "상환 예정일은 필수입니다.")
    private LocalDate repaymentDueDate;
}