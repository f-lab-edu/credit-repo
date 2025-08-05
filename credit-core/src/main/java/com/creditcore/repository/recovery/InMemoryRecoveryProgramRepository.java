package com.creditcore.repository.recovery;

import com.credit.common.recover.RecoverProgramStatus;
import com.creditcore.entity.RecoveryProgram;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRecoveryProgramRepository implements RecoveryProgramRepository{

    private final Map<String, RecoveryProgram> store = new ConcurrentHashMap<>();

    @Override
    public Optional<RecoveryProgram> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public RecoveryProgram save(RecoveryProgram recoveryProgram) {
        store.put(recoveryProgram.getId(), recoveryProgram);
        return recoveryProgram;
    }

    @Override
    public Optional<RecoveryProgram> findByContractIdAndStatus(String contractId, RecoverProgramStatus status) {
        return store.values().stream()
                .filter(program -> program.getContractId().equals(contractId))
                .filter(program -> program.getStatus() == status)
                .findFirst();
    }
}
