package com.creditcore.repository.recovery;

import com.credit.common.recover.RecoverProgramStatus;
import com.creditcore.entity.RecoveryProgram;

import java.util.Optional;

public interface RecoveryProgramRepository {

    Optional<RecoveryProgram> findById(String id);
    RecoveryProgram save(RecoveryProgram recoveryProgram);
    Optional<RecoveryProgram> findByContractIdAndStatus(String contractId, RecoverProgramStatus status);
}
