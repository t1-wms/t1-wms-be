package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OutboundRequestDto {
    private LocalDate outboundScheduleDate;

    private LocalDate planDate;

    private String productionPlanId;

    List<ProductInfoDto> productList;

    @Data
    @Builder
    @AllArgsConstructor
    public static class ProductInfoDto{
        private Long productId;

        private Integer productCount;
    }
}
