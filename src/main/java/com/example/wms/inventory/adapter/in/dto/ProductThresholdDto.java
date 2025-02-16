package com.example.wms.inventory.adapter.in.dto;

import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ProductThresholdDto {
    private Long productId;
    private String productCode;
    private String productName;
    private Integer productCount;
    private Integer productThreshold;
}
