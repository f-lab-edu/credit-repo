package com.credit.controller;

import com.credit.dto.request.ContractCreateRequest;
import com.credit.dto.response.ContractResponse;
import com.credit.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponse> createContract(@Valid @RequestBody ContractCreateRequest request) {
        log.info("계약 생성 요청 - lenderId: {}, borrowerId: {}", request.getLenderId(), request.getBorrowerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(contractService.createContract(request));
    }

    @PostMapping("/{contractId}/approve")
    public ResponseEntity<ContractResponse> approveContract(@PathVariable String contractId) {
        log.info("계약 승인 요청 - contractId: {}", contractId);
        return ResponseEntity.ok(contractService.approveContract(contractId));
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ContractResponse> getContract(@PathVariable String contractId) {
        return ResponseEntity.ok(contractService.getContract(contractId));
    }
}