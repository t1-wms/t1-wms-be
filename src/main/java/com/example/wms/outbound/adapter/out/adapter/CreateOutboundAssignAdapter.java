package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundMapper;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.port.out.CreateOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CreateOutboundAssignAdapter implements CreateOutboundAssignPort {

    private final OutboundMapper outboundMapper;

    @Override
    public void save(Outbound outbound) {
        outboundMapper.insert(outbound);
    }

    @Override
    public String findMaxOutboundAssignNumber() {
        return outboundMapper.findMaxOutboundAssignNumber();
    }

    @Override
    public int findOutboundAssign(Long outboundPlanId) {
        return outboundMapper.findOutboundAssignByPlanId(outboundPlanId);
    }
}
