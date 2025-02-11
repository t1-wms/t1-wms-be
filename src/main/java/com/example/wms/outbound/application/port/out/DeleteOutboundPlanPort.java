package com.example.wms.outbound.application.port.out;

public interface DeleteOutboundPlanPort {
    void deleteOutboundPlan(Long outboundPlanId);
    void deleteOutboundPlanAndProducts(Long outboundPlanId);
}
