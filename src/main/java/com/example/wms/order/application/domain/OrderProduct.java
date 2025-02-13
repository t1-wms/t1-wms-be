package com.example.wms.order.application.domain;


import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderProduct extends BaseEntity {
    private Long orderProductId;
    private Long orderId;
    private Long productId;
    private Boolean isDefective;
    private Long binId;

}
