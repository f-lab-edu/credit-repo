package com.creditexternalapi.toss.dto.request;

import lombok.Getter;

@Getter
public class TossWebhookPayload {
    private String status;
    private String orderId;
    private String transactionKey;
    private String createdAt;
}
