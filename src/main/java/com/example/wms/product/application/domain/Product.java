package com.example.wms.product.application.domain;

import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {
    private Long productId;
    private String productCode;
    private String productName;
    private Integer purchasePrice;
    private Integer salePrice;
    private Integer lotUnit;
    private Long supplierId;
    private Integer stockLotCount;
    private String category;
    private String minLotCount;
    private Integer leadTime;
    private String locationBinCode;
}
