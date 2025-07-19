package com.credit.common.common.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractStatus {
    PENDING_AGREEMENT("계약 동의 대기"),
    ACTIVE("활성화됨"), // 계약 활성화
    COMPLETED("완료됨"),
    FAILED("실패됨"),
    CANCELED("취소됨"),
    RECOVERY_PENDING_AGREEMENT("신뢰 회복 프로그램 동의 대기"),
    RECOVERY_IN_PROGRESS("신뢰 회복 프로그램 진행 중");
    
    private final String description;
}
