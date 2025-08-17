package com.creditcore.enums.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepaymentCycle {

    DAY("일일"),
    WEEKLY("주간"),
    MONTHLY("월간");

    private final String description;
}
