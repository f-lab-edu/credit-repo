package com.creditcore.enums.recover;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecoverProgramStatus {
    PENDING("제안 후 채무자 동의 대기"),
    IN_PROGRESS("신뢰 회복 프로그램 진행 중"),
    COMPLETED("신뢰 회복 프로그램 완료"),
    FINAL_FAILED("신뢰 회복 프로그램 실패");

    private final String description;
}
