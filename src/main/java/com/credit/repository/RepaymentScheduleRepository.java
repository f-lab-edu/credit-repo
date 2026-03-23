package com.credit.repository;

import com.credit.entity.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, String> {

    Optional<RepaymentSchedule> findFirstByContractId(String contractId);
}