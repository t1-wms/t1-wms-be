package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundRequestDto;

public interface CreateOutboundUseCase {
    void createOutbound(OutboundRequestDto outboundRequestDto);
}
