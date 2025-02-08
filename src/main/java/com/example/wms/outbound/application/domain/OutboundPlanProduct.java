package com.example.wms.outbound.application.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OutboundPlanProduct {
    private Long outboundPlanProductId;

    private Long outboundPlanId;

    private Long productId;

    private Integer requiredQuantity;

    private Integer StockUsedQuantity;

    private Integer OrderQuantity;

    private String status;
}
