package com.example.wms.inventory.application.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inventory {
    private Long inventoryId; // 재고 고유 ID
    private Long productId; // 제품 ID
    private Integer availableQuantity; // 사용 가능한 수량
    private LocalDateTime lastUpdated; // 마지막으로 업데이트된 일시
}
