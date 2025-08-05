package com.creditcore;

import com.credit.common.contract.ContractStatus;
import com.credit.common.contract.request.ContractCreateRequest;
import com.creditcore.entity.Contract;
import com.creditcore.repository.contract.ContractRepository;
import com.creditcore.service.contract.ContractService;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock // ContractRepository의 Mock 객체를 생성한다. 실제 구현체(InMemory...)가 아닌 가짜 객체
    private ContractRepository contractRepository; // Mock 객체 생성

    @InjectMocks // 테스트 대상인 ContractService 인스턴스를 생성하고, @Mock으로 생성된 Mock 객체들을 자동으로 주입한다.
    private ContractService contractService; // 테스트 대상 객체 생성 및 Mock 주입

    private ContractCreateRequest testRequest;
    private List<ContractStatus> activeStatues;

    @BeforeEach
    void setUp() {
        testRequest = ContractCreateRequest.builder()
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .build();

        activeStatues = ContractStatus.getActiveStatuses();
    }

    @Test
    @DisplayName("계약 생성 성공 - 유효한 요청")
    void createContract_success() {
        //Given
        when(contractRepository.findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                anyString(),
                any(BigDecimal.class),
                any(LocalDate.class),
                eq(activeStatues)
        )).thenReturn(Optional.empty());
        //쉽게 이해하자면 createContract()가 내부에서 사용하는 DB 접근 로직(findBy.., save)은 실제로 동작하지 않기 때문에
        // 그 동작을 이렇게 반응해라 고 미리 설정해줘야 정상 흐름으로 테스트가 진행된다.

        when(contractRepository.save(any(Contract.class)))
                .thenAnswer(invocation -> {
                    Contract contract = invocation.getArgument(0);
                    return Contract.builder()
                            .id("test-contract-id-" + contract.getBorrowerPhoneNumber())
                            .borrowerPhoneNumber(contract.getBorrowerPhoneNumber())
                            .principal(contract.getPrincipal())
                            .repaymentDate(contract.getRepaymentDate())
                            .status(ContractStatus.PENDING_AGREEMENT)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                });

        // When
        Contract createContract = contractService.createContract(testRequest);

        // then
        assertNotNull(createContract, "생성된 계약은 null이 아니어야 합니다.");
        assertNotNull(createContract.getId(), "생성된 계약의 ID는 null이 아니어야 합니다.");
        assertEquals(testRequest.getBorrowerPhoneNumber(), createContract.getBorrowerPhoneNumber(), "휴대폰 번호가 일치해야 합니다.");
        assertEquals(testRequest.getPrincipal(), createContract.getPrincipal(), "원금이 일치해야 합니다.");
        assertEquals(testRequest.getRepaymentDate(), createContract.getRepaymentDate(), "상환일이 일치해야 합니다.");
        assertEquals(ContractStatus.PENDING_AGREEMENT, createContract.getStatus(), "초기 상태는 PENDING_AGREEMENT여야 합니다.");

        verify(contractRepository, times(1)).findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                eq(testRequest.getBorrowerPhoneNumber()),
                eq(testRequest.getPrincipal()),
                eq(testRequest.getRepaymentDate()),
                eq(activeStatues)
        );

        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    @DisplayName("계약 생성 실패 - 중복 계약 존재")
    void createContract_fail_DuplicateContractExist() {
        // given

        Contract duplicateContract = Contract.builder()
                .id("duplicate-contract-id")
                .borrowerPhoneNumber(testRequest.getBorrowerPhoneNumber())
                .principal(testRequest.getPrincipal())
                .repaymentDate(testRequest.getRepaymentDate())
                .status(ContractStatus.PENDING_AGREEMENT)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        when(contractRepository.findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                anyString(),
                any(BigDecimal.class),
                any(LocalDate.class),
                eq(activeStatues)
        )).thenReturn(Optional.of(duplicateContract));

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contractService.createContract(testRequest);
        });

        verify(contractRepository, times(1)).findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                eq(testRequest.getBorrowerPhoneNumber()),
                eq(testRequest.getPrincipal()),
                eq(testRequest.getRepaymentDate()),
                eq(ContractStatus.getActiveStatuses())
        );

        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("계약 생성 실패 - 저장 실패")
    void createContract_fail_saveThrowEx() {
        // given

        when(contractRepository.findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                anyString(),
                any(BigDecimal.class),
                any(LocalDate.class),
                eq(ContractStatus.getActiveStatuses())
        )).thenReturn(Optional.empty());

        when(contractRepository.save(any(Contract.class)))
                .thenThrow(new RuntimeException("DB 저장 실패"));

        // when, then
        Assertions.assertThrows(RuntimeException.class, () -> {
           contractService.createContract(testRequest);
        });

        verify(contractRepository, times(1)).findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
                anyString(),
                any(BigDecimal.class),
                any(LocalDate.class),
                eq(activeStatues)
        );

        verify(contractRepository, times(1)).save(any(Contract.class));
    }


    @Test
    @DisplayName("계약 상세 조회 성공 - 계약이 존재 하는 경우")
    void getContractDetail_success() {
        String contractId = "test-contract-id";
        Contract contract = Contract.builder()
                .id(contractId)
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .status(ContractStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        //when
        Contract resultContract = contractService.getContractDetail(contractId);

        //then
        assertEquals(contractId, resultContract.getId());
        verify(contractRepository, times(1)).findById(contractId);
    }

    @Test
    @DisplayName("계약 상세 조회 실패 - 계약이 존재하지 않을 경우")
    void getContractDetail_fail_doseNotExist() {
        // given
        String nonContractId = "test-contract-id";

        when(contractRepository.findById(nonContractId)).thenReturn(Optional.empty());

        // when & Then
        assertThrows(IllegalArgumentException.class, () -> {
            contractService.getContractDetail(nonContractId);
        });

        verify(contractRepository, times(1)).findById(nonContractId);
    }

    @Test
    @DisplayName("계약 동의 성공 - 계약이 존재하고 상태가 PENDING_AGREEMENT 일 경우")
    void agreeContract_success() {
        // given
        String contractId = "agreed-contract-id";
        Contract mockContract = mock(Contract.class);

        when(mockContract.getId()).thenReturn(contractId);
        when(mockContract.getStatus()).thenReturn(ContractStatus.PENDING_AGREEMENT);
        when(mockContract.getBorrowerPhoneNumber()).thenReturn("010-9665-1195");
        when(mockContract.getPrincipal()).thenReturn(BigDecimal.valueOf(1000000));
        when(mockContract.getRepaymentDate()).thenReturn(LocalDate.of(2025, 8, 30));

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));
        when(contractRepository.save(any(Contract.class))).thenReturn(mockContract);

        //when
        Contract resultContract = contractService.agreeContract(contractId);

        //then
        assertEquals(mockContract.getId(), resultContract.getId());

        verify(contractRepository, times(1)).findById(contractId);
        verify(mockContract, times(1)).updateStatus(ContractStatus.ACTIVE);
        verify(contractRepository, times(1)).save(mockContract);
    }

    @Test
    @DisplayName("계약 동의 실패 - 계약을 찾을 수 없는 경우")
    void agreeContract_fail_ContractNotFound() {
        // given
        String nonContractId = "test-contract-id";

        when(contractRepository.findById(nonContractId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            contractService.agreeContract(nonContractId);
        });

        verify(contractRepository, times(1)).findById(nonContractId);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("계약 동의 실패 - 현재 계약 상태가 PENDING_AGREEMENT가 아닌 경우")
    void agreeContract_fail_statusIsNotPendingAgreement() {
        // given
        String contractId = "test-contract-id";
        Contract mockContract = mock(Contract.class);

        when(mockContract.getStatus()).thenReturn(ContractStatus.ACTIVE);
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            contractService.agreeContract(contractId);
        });

        verify(contractRepository, times(1)).findById(contractId);
        verify(mockContract, never()).updateStatus(any(ContractStatus.class));
        verify(contractRepository, never()).save(any(Contract.class));
    }
}
