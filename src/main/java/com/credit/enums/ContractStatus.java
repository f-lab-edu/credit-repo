package com.credit.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractStatus {
    PENDING("대기 - 채무자 승인 전"),
    PROCEEDING("진행 - 상환 대기 중"),
    COMPLETED("완료 - 상환 완료"),
    FAILED("불이행 - 상환일 경과");

    private final String description;
}