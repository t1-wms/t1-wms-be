package com.example.wms.inbound.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundCheckWorkerReqDto {
    private Long productId;
    private Boolean isDefective;
    private String scheduleNumber;
}
