package com.example.wms.order.application.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {
    private Long orderId;
    private Long supplierId;
    private LocalDateTime orderDate; //주문 날짜
    private LocalDateTime inboundDate; //입고 날짜
    private Boolean isApproved;
    private Boolean isDelayed;
    private String orderNumber;
    private Integer orderQuantity;
    private String orderStatus;
    private Long dailyPlanId;
    private Boolean isReturnOrder;
}
