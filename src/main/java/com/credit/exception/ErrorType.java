package com.credit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // Member 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    // Contract 관련
    CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "계약을 찾을 수 없습니다."),
    CONTRACT_NOT_PENDING(HttpStatus.CONFLICT, "승인 대기 상태의 계약이 아닙니다."),
    LENDER_NOT_FOUND(HttpStatus.NOT_FOUND, "채권자를 찾을 수 없습니다."),
    BORROWER_NOT_FOUND(HttpStatus.NOT_FOUND, "채무자를 찾을 수 없습니다."),
    SELF_CONTRACT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "본인과의 계약은 생성할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}