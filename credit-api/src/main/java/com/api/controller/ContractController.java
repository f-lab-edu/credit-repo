package com.api.controller;


import com.credit.common.contract.request.ContractCreateRequest;
import com.credit.common.contract.request.RecoveryProgramCreateRequest;
import com.credit.common.contract.response.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.credit.common.contract.ContractStatus.*;

@Slf4j
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
        log.info("채무자 휴대폰 : " + request.getBorrowerPhoneNumber() +
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
     * POST /{contractId}/agree : 계약 동의 (채무자)
     * Path Variable : contractId
     * Response : ContractAgreeResponse (200)
     */
    @PostMapping("/{contractId}/agree")
    public ResponseEntity<ContractAgreeResponse> agreeContract(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);

        ContractAgreeResponse response = ContractAgreeResponse.builder()
                .contractId(contractId)
                .status(ACTIVE)
                .build();
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

    /**
     * POST /{contractId}/deposit/virtual-account : 에스크로 원금 입금용 가상계좌 발급 요청 (채권자)
     * Path Variable : contractId
     * Response : VirtualAccountResponse (200)
     */
    @PostMapping("/{contractId}/deposit/virtual-account")
    public ResponseEntity<VirtualAccountResponse> depositVirtualAccount(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);

        VirtualAccountResponse response = VirtualAccountResponse.builder()
                .bankName("농협은행")
                .accountNumber("123-456-789012")
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     *  /{contractId}/repayment/virtual-account : 에스크로 상환 입금용 가상계좌 발급 요청 (채무자)
     *  Path Variable : contractId
     *  Response : VirtualAccountResponse (200)
     */
    @PostMapping("/{contractId}/repayment/virtual-account")
    public ResponseEntity<VirtualAccountResponse> repaymentVirtualAccount(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);

        VirtualAccountResponse response = VirtualAccountResponse.builder()
                .bankName("우리은행")
                .accountNumber("987-654-321089")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /{contractId}/recovery-programs : 신뢰 회복 프로그램 제안 (채권자)
     * Request Body : RecoveryProgramCreateRequest
     * Path Variable : contractId
     * Response : RecoveryProgramCreateResponse (200)
     */
    @PostMapping("/{contractId}/recovery-programs")
    public ResponseEntity<RecoveryProgramCreateResponse> createRecoveryProgram(
            @PathVariable String contractId,
            @Valid @RequestBody RecoveryProgramCreateRequest request
            ) {
        log.info("신뢰 회복 프로그램 제안 요청 - 계약 ID: " + contractId +
                ", 상환 횟수: " + request.getRepaymentCount() +
                ", 상환 주기: " + request.getRepaymentCycle().getDescription()); // Enum 값 사용

        RecoveryProgramCreateResponse response = RecoveryProgramCreateResponse.builder()
                .contractId(contractId)
                .status(RECOVERY_PENDING_AGREEMENT)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * POST /{contractId}/recovery-programs/agree : 신뢰 회복 프로그램 동의 (채무자)
     * Path Variable : contractId
     * Response : RecoveryProgramAgreeResponse (200)
     */
    @PostMapping("/{contractId}/recovery-programs/agree")
    public ResponseEntity<RecoveryProgramAgreeResponse> agreeRecoveryProgram(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);

        RecoveryProgramAgreeResponse response = RecoveryProgramAgreeResponse.builder()
                .contractId(contractId)
                .status(RECOVERY_IN_PROGRESS)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /{contractId}/proof : 법적 대응용 증거 자료(PDF)
     * Path Variable : contractId
     * Response : PDF 파일 스트림 (200)
     */
    @GetMapping("/{contractId}/proof")
    public ResponseEntity<byte[]> proofDocument(@PathVariable String contractId) {
        log.info("꼐약 ID : " + contractId);

        String dummyPdfContent = "PDF content 계약 ID :" + contractId;
        byte[] pdfContentBytes = dummyPdfContent.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(pdfContentBytes, headers, HttpStatus.OK);
    }

}
