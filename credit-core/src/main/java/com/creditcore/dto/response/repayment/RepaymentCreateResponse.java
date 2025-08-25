package com.creditcore.dto.response.repayment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentCreateResponse {
    private String repaymentId;
    private String orderId;
    private String virtualAccountNumber;
    private String bankCode;
    private String dueDate;

    public static RepaymentCreateResponse of(String repaymentId, String orderId,
                                            String vaNumber, String bankCode, String dueDate) {
        return RepaymentCreateResponse.builder()
                .repaymentId(repaymentId)
                .orderId(orderId)
                .virtualAccountNumber(vaNumber)
                .bankCode(bankCode)
                .dueDate(dueDate)
                .build();
    }
}
