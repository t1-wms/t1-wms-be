package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.application.domain.Inbound;

public interface InboundPort {
    void save(Inbound inbound);
    void delete(Long inboundId);
}
