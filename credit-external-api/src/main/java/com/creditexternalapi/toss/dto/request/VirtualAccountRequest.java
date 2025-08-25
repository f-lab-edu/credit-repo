package com.creditexternalapi.toss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualAccountRequest {
    private Integer amount;
    private String orderId; // 주문번호
    private String orderName; // 구매 상품
    private String customerName; //구매자명
    private String bank;
    private Integer validHours; //유효시간 최대 2160(90일), 값 넣지 않으면 168시간(7일)

    // 채무자 명의 계좌 발급 (대출 실행용)
    public static VirtualAccountRequest ofBorrower(String borrowerName, String contractId, Integer amount) {
        return VirtualAccountRequest.builder()
                .bank("088") // 신한
                .customerName(borrowerName)
                .amount(amount)
                .orderId(contractId)
                .orderName(amount + "원 대출")
                .validHours(168)
                .build();
    }

    // 채권자 명의 계좌 발급 (상환용)
    public static VirtualAccountRequest ofLender(String lenderName, String contractId, Integer amount) {
        return VirtualAccountRequest.builder()
                .bank("004") // 국민
                .customerName(lenderName)
                .amount(amount)
                .orderId(contractId + "-repay")
                .orderName(amount + "원 상환")
                .build();
    }
}
