package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPackingMapper;
import com.example.wms.outbound.application.port.out.DeleteOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOutboundPackingAdapter implements DeleteOutboundPackingPort {

    private final OutboundPackingMapper outboundPackingMapper;

    @Override
    public void deleteOutboundPacking(Long outboundId) {
        outboundPackingMapper.deleteOutboundPacking(outboundId);
    }
}
