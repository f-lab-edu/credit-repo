package com.creditcore.repository.repayment;

import com.creditcore.entity.Repayment;
import com.creditcore.enums.repayment.RepaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepaymentRepository extends JpaRepository<Repayment, String> {

    Optional<Repayment> findFirstByContractIdAndStatus(String contractId, RepaymentStatus status);

    boolean existsByOrderId(String repayOrderId);

    Optional<Repayment> findByOrderId(String repaymentOrderId);
}
