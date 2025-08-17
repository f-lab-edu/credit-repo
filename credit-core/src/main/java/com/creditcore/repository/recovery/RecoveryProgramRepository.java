package com.creditcore.repository.recovery;

import com.credit.common.recover.RecoverProgramStatus;
import com.creditcore.entity.RecoveryProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryProgramRepository extends JpaRepository<RecoveryProgram, String> {

    Optional<RecoveryProgram> findByContractIdAndStatus(String contractId, RecoverProgramStatus status);
}
