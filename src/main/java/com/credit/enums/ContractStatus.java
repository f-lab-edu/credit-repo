package com.credit.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractStatus {
    PENDING("대기 - 채무자 서명 전"),
    AGREED("서명 완료 - 채권자 확정 전"),
    PROCEEDING("진행 - 가상계좌 발급 완료, 상환 대기"),
    COMPLETED("완료 - 상환 완료"),
    FAILED("불이행 - 상환일 경과");

    private final String description;
}