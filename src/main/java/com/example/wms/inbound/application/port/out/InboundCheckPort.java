package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.application.domain.InboundCheck;

import java.util.List;

public interface InboundCheckPort {
    void save(InboundCheck inboundCheck);
    List<InboundCheck> findByInboundId(Long inboundId);
    void saveAll(List<InboundCheck> inboundChecks);
}
