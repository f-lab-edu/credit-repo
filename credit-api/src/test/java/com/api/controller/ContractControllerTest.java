package com.api.controller;

import com.credit.common.contract.request.ContractCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractController.class)
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("계약 생성 API - 정상 요청")
    void createContract_Success() throws Exception{
        //given
        ContractCreateRequest request = ContractCreateRequest.builder()
                .borrowerPhoneNumber("010-9665-1195")
                .principal(BigDecimal.valueOf(100000))
                .repaymentDate(LocalDate.of(2025, 8, 30))
                .build();

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.contractId").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.shareUrl").exists());
    }
}
