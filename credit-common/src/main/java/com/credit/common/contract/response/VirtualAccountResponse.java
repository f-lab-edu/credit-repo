package com.credit.common.contract.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualAccountResponse {

    private String bankName;
    private String accountNumber;
    private LocalDateTime expiresAt;
}
