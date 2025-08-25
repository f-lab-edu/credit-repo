package com.api.controller.webhook;

import com.creditcore.service.webhook.WebhookService;
import com.creditexternalapi.toss.dto.request.TossWebhookPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhook/toss")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<String> handle(@RequestBody TossWebhookPayload payload) {
        log.info("Toss Webhook : {}", payload);

        webhookService.handleVirtualAccountEvent(payload);
        return ResponseEntity.ok("ok");
    }
}
