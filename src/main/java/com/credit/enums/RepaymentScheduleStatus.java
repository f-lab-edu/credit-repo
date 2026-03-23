package com.credit.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepaymentScheduleStatus {
    PLANNED("상환 예정"),
    DONE("상환 완료"),
    OVERDUE("연체");

    private final String description;
}