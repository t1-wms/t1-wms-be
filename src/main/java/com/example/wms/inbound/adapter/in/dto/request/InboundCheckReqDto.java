package com.example.wms.inbound.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundCheckReqDto {
    private Long inboundId;
    private LocalDate checkDate;
    private String scheduleNumber;
    private List<InboundCheckedProductReqDto> checkedProductList;
}
