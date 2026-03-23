package com.credit.external.toss.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VirtualAccountResponse {

    private String accountNumber;
    private String bankCode;
    private String customerName;
    private String dueDate;
}
