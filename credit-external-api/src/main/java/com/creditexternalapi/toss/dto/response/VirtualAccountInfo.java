package com.creditexternalapi.toss.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VirtualAccountInfo {
    private String accountNumber;
    private String bankCode;
    private String customerName;
    private String dueDate;
}
