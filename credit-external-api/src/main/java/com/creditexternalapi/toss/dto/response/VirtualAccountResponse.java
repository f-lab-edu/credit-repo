package com.creditexternalapi.toss.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VirtualAccountResponse {
    private String orderId;
    private String orderName;
    private String status;
    private VirtualAccountInfo virtualAccount;
}
