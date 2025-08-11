package com.creditcore.dto.response.recovery;

import com.creditcore.enums.contract.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecoveryProgramCreateResponse {

    private String contractId;
    private ContractStatus status;

    public static RecoveryProgramCreateResponse from(String contractId, ContractStatus status) {
        return RecoveryProgramCreateResponse.builder()
                .contractId(contractId)
                .status(status)
                .build();
    }
}
