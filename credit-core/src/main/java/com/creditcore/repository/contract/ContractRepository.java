package com.creditcore.repository.contract;

import com.credit.common.contract.ContractStatus;
import com.creditcore.entity.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepository {

    Optional<Contract> findById(String id);

    Contract save(Contract contract);

    List<Contract> findAll();

    Optional<Contract> findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
            String borrowerPhoneNumber,
            BigDecimal principal,
            LocalDate repaymentDate,
            List<ContractStatus> statusList
    );
}
