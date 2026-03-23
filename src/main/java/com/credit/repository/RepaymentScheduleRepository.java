package com.credit.repository;

import com.credit.entity.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, String> {
}