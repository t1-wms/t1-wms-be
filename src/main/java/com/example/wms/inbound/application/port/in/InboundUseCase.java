package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;

public interface InboundUseCase {
    void createInboundPlan(InboundReqDto inboundReqDto);
}
