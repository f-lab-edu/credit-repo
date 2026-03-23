package com.credit.external.toss;

import com.credit.exception.CustomException;
import com.credit.exception.ErrorType;
import com.credit.external.toss.dto.VirtualAccountRequest;
import com.credit.external.toss.dto.VirtualAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class TossPaymentsClient {

    private final RestClient restClient;

    @Value("${toss.virtual-account.bank}")
    private String bank;

    @Value("${toss.virtual-account.valid-hours}")
    private int validHours;

    public TossPaymentsClient(@Value("${toss.secret-key}") String secretKey,
                              @Value("${toss.virtual-account.base-url}") String baseUrl) {
        String credentials = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Basic " + credentials)
                .build();
    }

    public VirtualAccountResponse issueVirtualAccount(String orderId, String orderName,
                                                      String customerName, long amount) {
        VirtualAccountRequest request = VirtualAccountRequest.builder()
                .orderId(orderId)
                .orderName(orderName)
                .customerName(customerName)
                .amount(amount)
                .bank(bank)
                .validHours(validHours)
                .build();

        try {
            return restClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(VirtualAccountResponse.class);
        } catch (Exception e) {
            log.error("토스페이먼츠 가상계좌 발급 실패 - orderId: {}, error: {}", orderId, e.getMessage());
            throw new CustomException(ErrorType.TOSS_PAYMENTS_ERROR);
        }
    }
}