package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.outbound.adapter.out.mapper.OutboundMapper;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOutboundAdapter implements CreateOutboundPort {

    private final OutboundMapper outboundMapper;

    @Override
    public void save(OutboundPlan outboundPlan) {
        outboundMapper.insert(outboundPlan);
    }
}
