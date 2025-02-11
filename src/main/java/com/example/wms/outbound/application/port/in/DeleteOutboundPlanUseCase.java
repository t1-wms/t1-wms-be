package com.example.wms.outbound.application.port.in;

public interface DeleteOutboundPlanUseCase {
    void deleteOutboundPlan(Long outboundPlanId);
    void deleteOutboundPlanAndProducts(Long outboundPlanId);
}
