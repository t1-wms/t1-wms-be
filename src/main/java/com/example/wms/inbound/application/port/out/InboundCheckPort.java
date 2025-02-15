package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.application.domain.InboundCheck;

public interface InboundCheckPort {
    void save(InboundCheck inboundCheck);
    void findByInboundId(Long inboundId);
}
