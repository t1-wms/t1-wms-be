package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;

public interface CreateOutboundAssignPort {
    void save(Outbound outbound);
    String findMaxOutboundAssignNumber();
    Outbound findOutboundByPlanId(Long outboundPlanId);
    void update(Outbound outbound);
}
