package com.creditcore.dto.response.contract;

import com.creditcore.entity.Contract;
import com.creditcore.enums.contract.ContractStatus;
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

    public static ContractCreateResponse of(Contract contract) {
        return ContractCreateResponse.builder()
                .contractId(contract.getId())
                .status(contract.getStatus())
                .shareUrl("https://promise.com/c/" + contract.getId())
                .build();
    }
}
