package com.example.wms.inbound.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundCheckUpdateReqDto {
    private Long inboundId;
    private String checkDate;
    private List<InboundCheckedProductReqDto> checkedProductList;
}
