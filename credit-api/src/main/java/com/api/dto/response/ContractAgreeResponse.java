package com.api.dto.response;

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
}
