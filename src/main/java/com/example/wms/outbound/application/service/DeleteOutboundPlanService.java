package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.DeleteOutboundPlanUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundPlanService implements DeleteOutboundPlanUseCase {

    private final DeleteOutboundPlanPort deleteOutboundPlanPort;

    @Override
    public void deleteOutboundPlan(Long outboundPlanId) {
        deleteOutboundPlanPort.deleteOutboundPlan(outboundPlanId);
    }

    @Override
    public void deleteOutboundPlanAndProducts(Long outboundPlanId) {
        deleteOutboundPlanPort.deleteOutboundPlanAndProducts(outboundPlanId);
    }
}
