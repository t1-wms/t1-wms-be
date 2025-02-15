package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;

public interface CreateOutboundPackingPort {
    void createOutboundPacking(Outbound outbound);
    Outbound findOutboundByPlanId(Long outboundPlanId);
    String findMaxOutboundPackingNumber();
}
