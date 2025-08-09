package com.creditcore.repository.contract;

import com.credit.common.contract.ContractStatus;
import com.creditcore.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, String> {

    Optional<Contract> findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
            String borrowerPhoneNumber,
            BigDecimal principal,
            LocalDate repaymentDate,
            List<ContractStatus> statusList
    );
}
