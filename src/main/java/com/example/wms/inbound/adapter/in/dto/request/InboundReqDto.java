package com.example.wms.inbound.adapter.in.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundReqDto {
    private LocalDateTime scheduleDate; // order의 delivery_deadLine 값을 저장하는 것임
    private Long orderId; // 주문 id
    private Long supplierId; // 공급처 번호
    private String orderNumber; // 주문 번호
    private LocalDateTime orderDate; // 주문 날짜
}
