package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPickingMapper;
import com.example.wms.outbound.application.port.out.GetOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOutboundPickingAdapter implements GetOutboundPickingPort {

    private final OutboundPickingMapper outboundPickingMapper;
}
