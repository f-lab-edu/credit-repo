package com.credit.dto.response;

import com.credit.entity.Contract;
import com.credit.entity.RepaymentSchedule;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class VirtualAccountIssuedResponse {

    private String contractId;
    private BigDecimal amount;
    private String borrowerName;
    private String virtualAccountNumber;
    private String bankCode;
    private String accountHolderName;
    private String accountDueDate;

    public static VirtualAccountIssuedResponse of(Contract contract, RepaymentSchedule schedule) {
        return VirtualAccountIssuedResponse.builder()
                .contractId(contract.getId())
                .amount(contract.getAmount())
                .borrowerName(contract.getBorrower().getName())
                .virtualAccountNumber(schedule.getVirtualAccountNumber())
                .bankCode(schedule.getBankCode())
                .accountHolderName(schedule.getAccountHolderName())
                .accountDueDate(schedule.getAccountDueDate())
                .build();
    }
}