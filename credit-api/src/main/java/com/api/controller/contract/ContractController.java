package com.api.controller.contract;


import com.creditcore.dto.request.contract.ContractCreateRequest;
import com.creditcore.dto.response.contract.ContractAgreeResponse;
import com.creditcore.dto.response.contract.ContractCreateResponse;
import com.creditcore.dto.response.contract.ContractDetailResponse;
import com.creditcore.service.contract.ContractService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    /**
     * POST / : 계약 생성 요청 (채권자)
     * Request Body : ContractCreateRequest
     * Response : ContractCreateResponse (201)
     */
    @PostMapping
    public ResponseEntity<ContractCreateResponse> createContract(
            @Valid @RequestBody ContractCreateRequest request){
        log.info("채무자 휴대폰 : " + request.getBorrowerPhoneNumber() +
                            ", 원금 : " + request.getPrincipal() +
                            ", 상환일 : " + request.getRepaymentDate());

        ContractCreateResponse response = contractService.createContract(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /{contractId}/agree : 계약 동의 (채무자)
     * Path Variable : contractId
     * Response : ContractAgreeResponse (200)
     */
    @PostMapping("/{contractId}/agree")
    public ResponseEntity<ContractAgreeResponse> agreeContract(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);
        ContractAgreeResponse response = contractService.agreeContract(contractId);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /{contractId} : 특정 계약서 상세 조회
     * Path Variable : contractId
     * Response : ContractDetailResponse (200)
     */
    @GetMapping("/{contractId}")
    public ResponseEntity<ContractDetailResponse> getContractDetail(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);
        ContractDetailResponse response = contractService.getContractDetail(contractId);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /{contractId}/cancel
     * Path Variable : contractId
     * 계약 상태 변경을 위한 Cancel API
     */
    @PostMapping("/{contractId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String contractId) {
        contractService.cancelContract(contractId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{contractId}/complete")
    public ResponseEntity<Void> complete(@PathVariable String contractId) {
        contractService.completeContract(contractId);
        return ResponseEntity.noContent().build();
    }

}
