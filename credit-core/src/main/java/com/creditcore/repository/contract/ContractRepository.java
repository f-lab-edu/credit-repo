package com.creditcore.repository.contract;

import com.creditcore.enums.contract.ContractStatus;
import com.creditcore.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

    Optional<Contract> findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
            String borrowerPhoneNumber,
            BigDecimal principal,
            LocalDate repaymentDate,
            List<ContractStatus> statusList
    );
}
