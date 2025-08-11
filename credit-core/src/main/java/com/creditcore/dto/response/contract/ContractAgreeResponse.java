package com.creditcore.dto.response.contract;

import com.creditcore.entity.Contract;
import com.creditcore.enums.contract.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractAgreeResponse {
    private String contractId;
    private ContractStatus status;

    public static ContractAgreeResponse of(Contract contract) {
        return ContractAgreeResponse.builder()
                .contractId(contract.getId())
                .status(contract.getStatus())
                .build();
    }
}
