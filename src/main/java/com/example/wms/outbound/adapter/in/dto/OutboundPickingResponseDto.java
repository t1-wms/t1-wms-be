package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OutboundPickingResponseDto {
    private Long outboundId;

    private Long outboundPlanId;

    private String process;

    private String outboundScheduleNumber;

    private String outboundAssignNumber;

    private String outboundPickingNumber;

    private LocalDate outboundPickingDate;

    private String productionPlanNumber;

    private LocalDate planDate;

    private List<ProductInfoDto> productList;
}
