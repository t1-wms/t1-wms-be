package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.mapper.InboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboundAdapter implements InboundPort {

    private final InboundMapper inboundMapper;

    @Override
    public void save(Inbound inbound) {
        inboundMapper.insert(inbound);
    }
}
