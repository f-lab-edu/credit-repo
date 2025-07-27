package com.credit.common.contract.response;

import com.credit.common.contract.ContractStatus;
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
}
