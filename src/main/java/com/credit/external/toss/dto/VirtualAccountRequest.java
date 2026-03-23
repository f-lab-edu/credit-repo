package com.credit.external.toss.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VirtualAccountRequest {

    private String orderId;
    private String orderName;
    private String customerName;
    private long amount;
    private String bank;
    private int validHours;
}
