package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.OutboundPlanProduct;

public interface DeleteOutboundPlanProductPort {
    void deleteOutboundPlanProduct(Long outboundPlanId);
}
