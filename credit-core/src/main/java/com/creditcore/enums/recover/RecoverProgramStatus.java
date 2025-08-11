package com.creditcore.enums.recover;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecoverProgramStatus {
    PENDING("제안 후 채무자 동의 대기"),
    APPROVED("채무자 동의 완료, 프로그램 진행 예정"),
    ONGOING("신뢰 회복 프로그램 진행 중"),
    COMPLETED("신뢰 회복 프로그램 완료"),
    REJECTED("채무자 거절"),
    CANCELLED("프로그램 취소됨");

    private final String description;
}
