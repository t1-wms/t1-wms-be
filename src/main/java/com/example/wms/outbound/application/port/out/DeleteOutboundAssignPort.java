package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;

public interface DeleteOutboundAssignPort {
    Outbound selectOutboundAssign(Long outboundId);
    void deleteOutboundAssign(Long outboundId);
}
