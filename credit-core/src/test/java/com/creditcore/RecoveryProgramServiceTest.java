package com.creditcore;

import com.credit.common.contract.ContractStatus;
import com.credit.common.contract.RepaymentCycle;
import com.credit.common.contract.request.RecoveryProgramCreateRequest;
import com.credit.common.recover.RecoverProgramStatus;
import com.creditcore.entity.Contract;
import com.creditcore.entity.RecoveryProgram;
import com.creditcore.repository.contract.ContractRepository;
import com.creditcore.repository.recovery.RecoveryProgramRepository;
import com.creditcore.service.recoveryprogram.RecoveryProgramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecoveryProgramServiceTest {

    @Mock
    private RecoveryProgramRepository recoveryProgramRepository;

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private RecoveryProgramService recoveryProgramService;

    private String testContractId;
    private RecoveryProgramCreateRequest testRequest;

    @BeforeEach
    void setUp() {
        testContractId = "contract-123";

        testRequest = RecoveryProgramCreateRequest.builder()
                .repaymentCount(3)
                .repaymentCycle(RepaymentCycle.MONTHLY)
                .build();
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 제안 성공 - 계약 상태")
    void createRecoveryProgram_success_activeContract() {
        // given
        Contract testContract = Contract.builder()
                .id(testContractId)
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .status(ContractStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(contractRepository.findById(testContractId)).thenReturn(Optional.of(testContract));
        when(recoveryProgramRepository.save(any(RecoveryProgram.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        recoveryProgramService.createRecoveryProgram(testContractId, testRequest);

        // then
        verify(contractRepository).findById(testContractId);
        verify(recoveryProgramRepository).save(any(RecoveryProgram.class));
        verify(contractRepository).save(testContract);

        assertEquals(ContractStatus.RECOVERY_PENDING_AGREEMENT, testContract.getStatus());
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 제안 실패 - 계약을 찾을 수 없음")
    void createRecoveryProgram_fail_contractNotFound() {
        // given
        when(contractRepository.findById(testContractId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                recoveryProgramService.createRecoveryProgram(testContractId, testRequest));

        verify(recoveryProgramRepository, never()).save(any(RecoveryProgram.class));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 동의 성공")
    void agreeRecoveryProgram_success() {
        // given
        Contract testContract = Contract.builder()
                .id(testContractId)
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .status(ContractStatus.RECOVERY_PENDING_AGREEMENT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        RecoveryProgram pendingProgram = RecoveryProgram.create(testContractId, 3, RepaymentCycle.MONTHLY);

        when(contractRepository.findById(testContractId)).thenReturn(Optional.of(testContract));
        when(recoveryProgramRepository.findByContractIdAndStatus(testContractId, RecoverProgramStatus.PENDING))
                .thenReturn(Optional.of(pendingProgram));
        when(recoveryProgramRepository.save(any(RecoveryProgram.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(contractRepository.save(any(Contract.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // When
        RecoveryProgram agreedProgram = recoveryProgramService.agreeRecoveryProgram(testContractId);

        // Then
        assertEquals(RecoverProgramStatus.APPROVED, agreedProgram.getStatus());
        assertEquals(ContractStatus.RECOVERY_IN_PROGRESS, testContract.getStatus());

        verify(contractRepository).findById(testContractId);
        verify(recoveryProgramRepository).findByContractIdAndStatus(testContractId, RecoverProgramStatus.PENDING);
        verify(recoveryProgramRepository).save(pendingProgram);
        verify(contractRepository).save(testContract);
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 동의 실패 - 계약을 찾을 수 없음")
    void agreeRecoveryProgram_fail_contractNotFound() {
        // given
        when(contractRepository.findById(testContractId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                recoveryProgramService.agreeRecoveryProgram(testContractId));

        verify(recoveryProgramRepository, never()).findByContractIdAndStatus(anyString(), any());
        verify(recoveryProgramRepository, never()).save(any(RecoveryProgram.class));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 동의 실패 - 계약 상태 부적절 (ACTIVE)")
    void agreeRecoveryProgram_fail_contractStatusInvalid_active() {
        // given
        Contract testContract = Contract.builder()
                .id(testContractId)
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .status(ContractStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(contractRepository.findById(testContractId)).thenReturn(Optional.of(testContract));

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                recoveryProgramService.agreeRecoveryProgram(testContractId));

        verify(recoveryProgramRepository, never()).findByContractIdAndStatus(anyString(), any());
        verify(recoveryProgramRepository, never()).save(any(RecoveryProgram.class));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("신뢰 회복 프로그램 동의 실패 - 동의 대기 중인 프로그램이 없음")
    void agreeRecoveryProgram_fail_noPendingProgram() {
        // given
        Contract testContract = Contract.builder()
                .id(testContractId)
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .status(ContractStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(contractRepository.findById(testContractId)).thenReturn(Optional.of(testContract));
        when(recoveryProgramRepository.findByContractIdAndStatus(testContractId, RecoverProgramStatus.PENDING))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                recoveryProgramService.agreeRecoveryProgram(testContractId));

        verify(recoveryProgramRepository).findByContractIdAndStatus(testContractId, RecoverProgramStatus.PENDING);
        verify(recoveryProgramRepository, never()).save(any(RecoveryProgram.class));
        verify(contractRepository, never()).save(any(Contract.class));
    }

}
