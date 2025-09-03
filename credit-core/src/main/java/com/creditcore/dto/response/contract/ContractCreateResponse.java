package com.creditcore.dto.response.contract;

import com.creditcore.entity.Contract;
import com.creditcore.enums.contract.ContractStatus;
import com.creditexternalapi.toss.dto.request.VirtualAccountRequest;
import com.creditexternalapi.toss.dto.response.VirtualAccountResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractCreateResponse {

    private String contractId;
    private ContractStatus status;
    private String shareUrl;

    //가상계좌 정보 추가
    private String virtualAccountNumber;
    private String virtualAccountBankCode;
    private String virtualAccountStatus;

    public static ContractCreateResponse of(Contract contract, VirtualAccountResponse response) {
        return ContractCreateResponse.builder()
                .contractId(contract.getId())
                .status(contract.getStatus())
                .shareUrl("https://promise.com/c/" + contract.getId())
                .virtualAccountNumber(response.getVirtualAccount().getAccountNumber())
                .virtualAccountBankCode(response.getVirtualAccount().getBankCode())
                .virtualAccountStatus(response.getStatus())
                .build();
    }
}
