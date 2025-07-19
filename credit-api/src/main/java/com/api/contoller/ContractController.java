package com.api.contoller;


import com.credit.common.contract.request.ContractCreateRequest;
import com.credit.common.contract.response.ContractCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
