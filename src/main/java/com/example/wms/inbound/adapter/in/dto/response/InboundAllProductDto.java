package com.example.wms.inbound.adapter.in.dto.response;

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
public class InboundAllProductDto {
    private Long inboundId;
    private String inboundStatus;
    private LocalDate createdAt;
    private String scheduleNumber;
    private LocalDate scheduleDate;
    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Long supplierId;
    private String supplierName;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer productCount;
    private Integer lotCount;
}
