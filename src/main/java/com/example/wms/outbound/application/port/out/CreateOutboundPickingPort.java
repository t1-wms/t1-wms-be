package com.example.wms.outbound.application.port.out;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.outbound.application.domain.Outbound;

public interface CreateOutboundPickingPort {
    void createOutboundPicking(Outbound outbound);
    Outbound findOutboundByPlanId(Long outboundPlanId);
    String findMaxOutboundPickingNumber();
}
