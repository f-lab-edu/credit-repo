package com.credit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;

    public static ErrorResponse of(ErrorType errorType) {
        return new ErrorResponse(errorType.getHttpStatus(), errorType.getMessage());
    }
}