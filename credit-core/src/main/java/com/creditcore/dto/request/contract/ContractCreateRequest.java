package com.creditcore.dto.request.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractCreateRequest {

    @NotBlank
    private String lenderId;

    @NotBlank
    private String borrowerId;

    @NotBlank(message = "채무자 휴대폰 번호는 필수입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "유효하지 않은 휴대폰 번호 형식입니다.")
    private String borrowerPhoneNumber; // 채무자 휴대폰 번호

    @NotNull(message = "원금은 필수입니다.")
    private BigDecimal principal; // 원금

    @NotNull(message = "상환일은 필수입니다.")
    private LocalDate repaymentDate; // 상환일
}
