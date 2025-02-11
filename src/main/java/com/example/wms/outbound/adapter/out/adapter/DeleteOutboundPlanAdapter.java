package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanMapper;
import com.example.wms.outbound.application.port.out.DeleteOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOutboundPlanAdapter implements DeleteOutboundPlanPort {

    private final OutboundPlanMapper outboundPlanMapper;

    @Override
    public void deleteOutboundPlan(Long outboundPlanId) {
        outboundPlanMapper.deleteById(outboundPlanId);
    }

    @Override
    public void deleteOutboundPlanAndProducts(Long outboundPlanId) {
        outboundPlanMapper.deleteOutboundPlanAndProducts(outboundPlanId);
    }
}
