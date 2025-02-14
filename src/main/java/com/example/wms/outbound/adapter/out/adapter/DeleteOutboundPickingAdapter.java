package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPickingMapper;
import com.example.wms.outbound.application.port.out.DeleteOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOutboundPickingAdapter implements DeleteOutboundPickingPort {

    private final OutboundPickingMapper outboundPickingMapper;

    @Override
    public void deleteOutboundPicking(Long outboundId) {
        outboundPickingMapper.deleteOutboundPicking(outboundId);
    }
}
