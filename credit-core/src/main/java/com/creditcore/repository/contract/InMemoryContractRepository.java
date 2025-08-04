package com.creditcore.repository.contract;

import com.credit.common.contract.ContractStatus;
import com.credit.common.contract.request.ContractCreateRequest;
import com.creditcore.entity.Contract;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Repository
public class InMemoryContractRepository implements ContractRepository {

    private final Map<String, Contract> store = new HashMap<>();

    @Override
    public Optional<Contract> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Contract save(Contract contract) {
        store.put(contract.getId(), contract);
        return contract;
    }

    @Override
    public List<Contract> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Contract> findByBorrowerPhoneNumberAndPrincipalAndRepaymentDateAndStatusIn(
            String borrowerPhoneNumber,
            BigDecimal principal,
            LocalDate repaymentDate,
            List<ContractStatus> statusList) {
        return store.values().stream()
                .filter(contract -> contract.getBorrowerPhoneNumber().equals(borrowerPhoneNumber))
                .filter(contract -> contract.getPrincipal().equals(principal))
                .filter(contract -> contract.getRepaymentDate().equals(repaymentDate))
                .filter(contract -> statusList.contains(contract.getStatus()))
                .findFirst();
    }
}
