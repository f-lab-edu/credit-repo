package com.api.contoller;


import com.credit.common.contract.request.ContractCreateRequest;
import com.credit.common.contract.response.ContractCreateResponse;
import com.credit.common.contract.response.ContractDetailResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.credit.common.contract.ContractStatus.*;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {

    /**
     * POST / : 계약 생성 요청 (채권자)
     * Request Body : ContractCreateRequest
     * Response : ContractCreateResponse (201)
     */
    @PostMapping
    public ResponseEntity<ContractCreateResponse> createContract(
            @Valid @RequestBody ContractCreateRequest request) {
        System.out.println("채무자 휴대폰 : " + request.getBorrowerPhoneNumber() +
                            ", 원금 : " + request.getPrincipal() +
                            ", 상환일 : " + request.getRepaymentDate());

        // 임시 더미데이터 반환
        ContractCreateResponse response = ContractCreateResponse.builder()
                .contractId("contractUUID")
                .status(PENDING_AGREEMENT)
                .shareUrl("https://app.com/c/contractUUID")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /{contractId} : 특정 계약서 상세 조회
     * Path Variable : contractId
     * Response : ContractDetailResponse (200)
     */
    @GetMapping("/{contractId}")
    public ResponseEntity<ContractDetailResponse> getContractDetail(@PathVariable String contractId) {
        System.out.println("계약 ID : " + contractId);

        ContractDetailResponse response = ContractDetailResponse.builder()
                .contractId(contractId)
                .lenderName("채권자")
                .borrowerName("채무자")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 7, 30))
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 7, 19, 18, 33, 25))
                .build();

        return ResponseEntity.ok(response);
    }
}
