package com.api.controller.recovery;

import com.creditcore.dto.request.recovery.RecoveryProgramCreateRequest;
import com.creditcore.dto.response.recovery.RecoveryProgramAgreeResponse;
import com.creditcore.dto.response.recovery.RecoveryProgramCreateResponse;
import com.creditcore.service.recoveryprogram.RecoveryProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("{contractId}/recovery-program")
@RequiredArgsConstructor
@Slf4j
public class RecoveryProgramController {

    private final RecoveryProgramService recoveryProgramService;

    /**
     * POST 신뢰 회복 프로그램 제안 (채권자)
     * Request Body : RecoveryProgramCreateRequest
     * Path Variable : contractId
     * Response : RecoveryProgramCreateResponse (200)
     */
    @PostMapping
    public ResponseEntity<RecoveryProgramCreateResponse> createRecoveryProgram(
            @PathVariable String contractId,
            @Valid @RequestBody RecoveryProgramCreateRequest request
    ) {
        log.info("신뢰 회복 프로그램 제안 요청 - 계약 ID: " + contractId +
                ", 상환 횟수: " + request.getRepaymentCount() +
                ", 상환 주기: " + request.getRepaymentCycle().getDescription());

        RecoveryProgramCreateResponse response = recoveryProgramService.createRecoveryProgram(contractId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /agree : 신뢰 회복 프로그램 동의 (채무자)
     * Path Variable : contractId
     * Response : RecoveryProgramAgreeResponse (200)
     */
    @PostMapping("/agree")
    public ResponseEntity<RecoveryProgramAgreeResponse> agreeRecoveryProgram(@PathVariable String contractId) {
        log.info("계약 ID : " + contractId);

        RecoveryProgramAgreeResponse response = recoveryProgramService.agreeRecoveryProgram(contractId);
        return ResponseEntity.ok(response);
    }
}
