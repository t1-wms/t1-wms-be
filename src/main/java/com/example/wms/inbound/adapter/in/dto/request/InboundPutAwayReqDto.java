package com.example.wms.inbound.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundPutAwayReqDto {

    private Long productId;
    private Integer lotCount;
    private Long binId;
}
