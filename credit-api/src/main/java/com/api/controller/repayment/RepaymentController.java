package com.api.controller.repayment;

import com.creditcore.service.repayment.RepaymentService;
import com.creditexternalapi.toss.dto.response.VirtualAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts/{contractId}/repayment")
@RequiredArgsConstructor
public class RepaymentController {

    private final RepaymentService repaymentService;

    @PostMapping("/virtual-account")
    public ResponseEntity<VirtualAccountResponse> createVirtualAccount(@PathVariable String contractId) {
        VirtualAccountResponse response = repaymentService.createRepaymentVirtualAccount(contractId);
        return ResponseEntity.ok(response);
    }
}
