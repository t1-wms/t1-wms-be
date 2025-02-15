package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.mapper.InboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InboundAdapter implements InboundPort {

    private final InboundMapper inboundMapper;

    @Override
    public void save(Inbound inbound) {
        inboundMapper.insert(inbound);
    }

    @Override
    public void delete(Long inboundId) {
        inboundMapper.delete(inboundId);
    }

    @Override
    public Inbound findById(Long inboundId) {
        return inboundMapper.findById(inboundId);
    }

    @Override
    public void updateIC(Long inboundId, LocalDate checkDate, String checkNumber) {
        inboundMapper.updateIC(inboundId, checkDate, checkNumber);
    }

}
