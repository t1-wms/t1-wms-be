package com.example.wms.order.application.domain;


import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderProduct {
    private Long orderProductId;
    private Long orderId;
    private Integer productCount;
    private Long productId;
    private Boolean isDefective;
}
