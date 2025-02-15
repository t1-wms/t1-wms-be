package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;

public interface CreateOutboundLoadingPort {
    void createOutboundLoading(Outbound outbound);
    Outbound findOutboundByPlanId(Long outboundPlanId);
    String findMaxOutboundLoadingNumber();
}
