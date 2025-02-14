package com.example.wms.inbound.adapter.in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundProductDto {
    private Long productId;
    private String productCode;
    private String productName;
    private Integer productCount;
    private Integer lotCount;
}
