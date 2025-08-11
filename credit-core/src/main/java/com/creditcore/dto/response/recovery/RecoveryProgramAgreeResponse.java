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
public class RecoveryProgramAgreeResponse {
    private String contractId;
    private ContractStatus status;

    public static RecoveryProgramAgreeResponse from(String contractId, ContractStatus status) {
        return RecoveryProgramAgreeResponse.builder()
                .contractId(contractId)
                .status(status)
                .build();
    }
}
