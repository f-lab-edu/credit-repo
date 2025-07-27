package com.api.controller;

import com.api.CreditApiApplication;
import com.credit.common.contract.ContractStatus;
import com.credit.common.contract.RepaymentCycle;
import com.credit.common.contract.request.ContractCreateRequest;
import com.credit.common.contract.request.RecoveryProgramCreateRequest;
import com.credit.common.contract.response.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        ,classes = CreditApiApplication.class)
public class ContractControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ContractControllerTest.class);
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("계약 생성 API - 정상 요청")
    void createContract_Success() {
        //given
        ContractCreateRequest request = ContractCreateRequest.builder()
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .build();

        //when
        ResponseEntity<ContractCreateResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts",
                request,
                ContractCreateResponse.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContractId()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(ContractStatus.PENDING_AGREEMENT);
        assertThat(response.getBody().getShareUrl()).isNotNull();
    }

    @Test
    @DisplayName("계약 생성 API - 유효성 검사 실패(필수값 누락)")
    void createContract_ValidationFail() {
        //given
        ContractCreateRequest request = ContractCreateRequest.builder()
                .borrowerPhoneNumber(null)
                .principal(null)
                .repaymentDate(null)
                .build();

        //when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts",
                request,
                String.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("계약 생성 API - 실패(채무자 전화번호 유효하지 않은 형식)")
    void createContract_borrowerPhoneNumber_InvalidPattern() {
        // given
        ContractCreateRequest request = ContractCreateRequest.builder()
                .borrowerPhoneNumber("010-1234-567") // @Pattern 위반
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 3))
                .build();

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts",
                request,
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        // GlobalException 구현 후 진행 예정, 현재는 에러 메시지 출력 X
        // assertThat(response.getBody()).contains("유효하지 않은 휴대폰 번호 형식입니다.");
    }

    @Test
    @DisplayName("계약 동의(채무자) - 정상 요청")
    void agreeContract_Success() {
        //given
        String contractId = "1234-5678-abc";

        //when
        ResponseEntity<ContractAgreeResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/agree",
                null,
                ContractAgreeResponse.class,
                contractId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContractId()).isEqualTo(contractId);
        assertThat(response.getBody().getStatus()).isEqualTo(ContractStatus.ACTIVE);

    }

    @Test
    @DisplayName("특정 계약 상세 조회 - 정상 요청")
    void getDetailContract_Success() {
        //given
        String contractId = "1234-5678-ABC";

        //when
        ResponseEntity<ContractDetailResponse> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}",
                ContractDetailResponse.class,
                contractId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContractId()).isEqualTo(contractId);
        assertThat(response.getBody().getStatus()).isEqualTo(ContractStatus.ACTIVE);
        assertThat(response.getBody().getLenderName()).isEqualTo("채권자");
        assertThat(response.getBody().getBorrowerName()).isEqualTo("채무자");
        assertThat(response.getBody().getPrincipal()).isEqualTo(BigDecimal.valueOf(100000));
        assertThat(response.getBody().getRepaymentDate()).isEqualTo(LocalDate.of(2025, 7, 30));
        assertThat(response.getBody().getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 7, 19, 18, 33, 25));
    }

    @Test
    @DisplayName("원금 입금용 가상계좌 발급 요청 API 테스트 - 성공")
    void depositVirtualAccount_Success() {
        // given
        String contractId = "1234-5678-abc";

        // When
        ResponseEntity<VirtualAccountResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/deposit/virtual-account",
                null, // 요청 본문 없음
                VirtualAccountResponse.class,
                contractId
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBankName()).isEqualTo("농협은행");
        assertThat(response.getBody().getAccountNumber()).isEqualTo("123-456-789012");
        assertThat(response.getBody().getExpiresAt()).isNotNull();
    }

    @Test
    @DisplayName("상환용 가상계좌 발급 요청 API 테스트 - 성공")
    void repaymentVirtualAccount_Success() {
        // given
        String contractId = "1234-5678-abc";

        // When
        ResponseEntity<VirtualAccountResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/repayment/virtual-account",
                null, // 요청 본문 없음
                VirtualAccountResponse.class,
                contractId
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBankName()).isEqualTo("우리은행");
        assertThat(response.getBody().getAccountNumber()).isEqualTo("987-654-321089");
        assertThat(response.getBody().getExpiresAt()).isNotNull();
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 제안 - 실패(repaymentCount 필수값 누락")
    void createRecoveryProgram_repaymentCount_null() {

        // given
        String contractId = "1234-5678-abc";

        RecoveryProgramCreateRequest request = RecoveryProgramCreateRequest.builder()
                .repaymentCount(null) // @NotNull 위반
                .repaymentCycle(RepaymentCycle.WEEKLY)
                .build();

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/recovery-programs",
                request,
                String.class,
                contractId
        );
        System.out.println("실제 응답 본문 : " + response.getBody());
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        // GlobalException 추가 후 진행 예정, 현재 ResponseBody에는 오류 메시지 출력 X
//        assertThat(response.getBody()).contains("상환 횟수는 필수 입니다.");
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 제안 - 실패(repaymentCount 최소값 미만)")
    void createRecoveryProgram_repaymentCount_LessThenMin() {

        // given
        String contractId = "1234-5678-abc";

        RecoveryProgramCreateRequest request = RecoveryProgramCreateRequest.builder()
                .repaymentCount(0) // @Min(1) 위반
                .repaymentCycle(RepaymentCycle.WEEKLY)
                .build();

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/recovery-programs",
                request,
                String.class,
                contractId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        // GlobalException 추가 후 진행 예정, 현재 ResponseBody에는 오류 메시지 출력 X
//        assertThat(response.getBody()).contains("상환 횟수는 최소 1이상이어야 합니다.");
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 제안 - 실패(repaymentCycle 필수값 누락)")
    void createRecoveryProgram_repaymentCycle_Null() {

        // given
        String contractId = "1234-5678-abc";

        RecoveryProgramCreateRequest request = RecoveryProgramCreateRequest.builder()
                .repaymentCount(2)
                .repaymentCycle(null) // @NotNull 위반
                .build();

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/recovery-programs",
                request,
                String.class,
                contractId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        // GlobalException 추가 후 진행 예정, 현재 ResponseBody에는 오류 메시지 출력 X
//        assertThat(response.getBody()).contains("상환 주기는 필수 입니다.");
    }



    @Test
    @DisplayName("신뢰 회복 프로그램 제안 API 테스트 - 성공")
    void recoveryProgram_Success() {
        // given
        String contractId = "1234-5678-abc";

        RecoveryProgramCreateRequest request = RecoveryProgramCreateRequest.builder()
                .repaymentCount(5)
                .repaymentCycle(RepaymentCycle.WEEKLY)
                .build();

        // When
        ResponseEntity<RecoveryProgramCreateResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/recovery-programs",
                request,
                RecoveryProgramCreateResponse.class,
                contractId
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContractId()).isEqualTo(contractId);
        assertThat(response.getBody().getStatus()).isEqualTo(ContractStatus.RECOVERY_PENDING_AGREEMENT);
    }

    @Test
    @DisplayName("법적 대응용 증거 자료 다운로드 API 테스트 - 성공")
    void downloadProofPdf_Success() {
        // given
        String contractId = "1234-5678-abc";

        // When
        ResponseEntity<byte[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/contracts/{contractId}/proof",
                byte[].class,
                contractId
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
        assertThat(response.getHeaders().getContentType()).isEqualTo(org.springframework.http.MediaType.APPLICATION_PDF);
    }

}
