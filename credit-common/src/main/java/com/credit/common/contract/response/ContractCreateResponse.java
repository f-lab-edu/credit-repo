package com.credit.common.contract.response;

import com.credit.common.common.contract.ContractStatus;
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
}
