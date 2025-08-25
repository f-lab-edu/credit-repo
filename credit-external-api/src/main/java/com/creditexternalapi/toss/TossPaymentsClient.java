package com.creditexternalapi.toss;

import com.creditexternalapi.toss.dto.request.VirtualAccountRequest;
import com.creditexternalapi.toss.dto.response.VirtualAccountResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class TossPaymentsClient {

    private final RestClient restClient;

    @Value("${toss.secret-key}")
    private String secretKey;
    @Value("${toss.virtual-account.base-url}")
    private String baseUrl;

    public VirtualAccountResponse createVirtualAccount(VirtualAccountRequest request){
        return restClient.post()
                .uri(baseUrl)
                .header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(VirtualAccountResponse.class);
    }

    private String buildAuthorizationHeader() {
        String auth = secretKey + ":";
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }
}
