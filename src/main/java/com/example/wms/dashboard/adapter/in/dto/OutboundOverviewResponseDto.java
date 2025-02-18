package com.example.wms.dashboard.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OutboundOverviewResponseDto {
    private Integer outboundSchedule;
    private Integer outboundAssign;
    private Integer outboundPicking;
    private Integer outboundPacking;
    private Integer outboundLoading;
}
