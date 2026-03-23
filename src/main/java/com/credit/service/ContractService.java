package com.credit.service;

import com.credit.dto.request.ContractCreateRequest;
import com.credit.dto.response.ContractResponse;
import com.credit.dto.response.VirtualAccountIssuedResponse;
import com.credit.entity.Contract;
import com.credit.entity.Member;
import com.credit.entity.RepaymentSchedule;
import com.credit.enums.ContractStatus;
import com.credit.exception.CustomException;
import com.credit.exception.ErrorType;
import com.credit.external.toss.TossPaymentsClient;
import com.credit.external.toss.dto.VirtualAccountResponse;
import com.credit.repository.ContractRepository;
import com.credit.repository.MemberRepository;
import com.credit.repository.RepaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final MemberRepository memberRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final TossPaymentsClient tossPaymentsClient;

    /**
     * 계약 생성 (채권자가 요청)
     * - Contract 생성 (PENDING 상태)
     * - RepaymentSchedule 자동 생성 (PLANNED 상태)
     */
    @Transactional
    public ContractResponse createContract(ContractCreateRequest request) {
        Member lender = memberRepository.findById(request.getLenderId())
                .orElseThrow(() -> new CustomException(ErrorType.LENDER_NOT_FOUND));
        Member borrower = memberRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new CustomException(ErrorType.BORROWER_NOT_FOUND));

        if (lender.getId().equals(borrower.getId())) {
            throw new CustomException(ErrorType.SELF_CONTRACT_NOT_ALLOWED);
        }

        Contract contract = Contract.create(lender, borrower, request.getAmount(), request.getRepaymentDueDate());
        contractRepository.save(contract);

        RepaymentSchedule schedule = RepaymentSchedule.create(contract);
        repaymentScheduleRepository.save(schedule);

        log.info("계약 생성 완료 - contractId: {}, lender: {}, borrower: {}",
                contract.getId(), lender.getName(), borrower.getName());

        return ContractResponse.from(contract);
    }

    /**
     * 계약 서명 (채무자가 요청)
     * - Contract 상태: PENDING → AGREED
     */
    @Transactional
    public ContractResponse approveContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new CustomException(ErrorType.CONTRACT_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.PENDING) {
            throw new CustomException(ErrorType.CONTRACT_NOT_PENDING);
        }

        contract.updateStatus(ContractStatus.AGREED);

        log.info("계약 서명 완료 - contractId: {}, status: {}", contract.getId(), contract.getStatus());

        return ContractResponse.from(contract);
    }

    /**
     * 계약 확정 (채권자가 요청)
     * - Contract 상태: AGREED → PROCEEDING
     * - TossPayments 가상계좌 발급
     * - RepaymentSchedule에 가상계좌 정보 저장
     */
    @Transactional
    public VirtualAccountIssuedResponse confirmContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new CustomException(ErrorType.CONTRACT_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.AGREED) {
            throw new CustomException(ErrorType.CONTRACT_NOT_AGREED);
        }

        RepaymentSchedule schedule = repaymentScheduleRepository.findFirstByContractId(contractId)
                .orElseThrow(() -> new CustomException(ErrorType.REPAYMENT_SCHEDULE_NOT_FOUND));

        if (schedule.isVirtualAccountIssued()) {
            throw new CustomException(ErrorType.VIRTUAL_ACCOUNT_ALREADY_ISSUED);
        }

        String orderName = contract.getBorrower().getName() + "의 채무 상환";
        VirtualAccountResponse tossResponse = tossPaymentsClient.issueVirtualAccount(
                contract.getOrderId(),
                orderName,
                contract.getBorrower().getName(),
                contract.getAmount().longValue()
        );

        schedule.assignVirtualAccount(
                tossResponse.getAccountNumber(),
                tossResponse.getBankCode(),
                tossResponse.getCustomerName(),
                tossResponse.getDueDate()
        );

        contract.updateStatus(ContractStatus.PROCEEDING);

        log.info("계약 확정 완료 - contractId: {}, accountNumber: {}",
                contract.getId(), tossResponse.getAccountNumber());

        return VirtualAccountIssuedResponse.of(contract, schedule);
    }

    @Transactional(readOnly = true)
    public ContractResponse getContract(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new CustomException(ErrorType.CONTRACT_NOT_FOUND));

        return ContractResponse.from(contract);
    }
}