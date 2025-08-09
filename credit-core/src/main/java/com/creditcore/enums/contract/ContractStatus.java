package com.creditcore.enums.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

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

    //비지니스 로직에서 활성으로 간주하는 상태들의 리스트를 제공하는 정적 메서드
    public static List<ContractStatus> getActiveStatuses() {
        return Arrays.asList(
                PENDING_AGREEMENT,
                ACTIVE,
                RECOVERY_IN_PROGRESS
        );
    }
}
