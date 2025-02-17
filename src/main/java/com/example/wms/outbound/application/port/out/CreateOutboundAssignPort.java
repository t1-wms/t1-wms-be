package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;

public interface CreateOutboundAssignPort {
    void save(Outbound outbound);
    String findMaxOutboundAssignNumber();
    Outbound findOutboundByPlanId(Long outboundPlanId);
    void update(Outbound outbound);
    void updateOutboundPlanStatus(OutboundPlan outboundPlan);
}
