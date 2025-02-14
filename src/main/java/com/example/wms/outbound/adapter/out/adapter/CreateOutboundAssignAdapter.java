package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundMapper;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.port.out.CreateOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
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
    public Outbound findOutboundByPlanId(Long outboundPlanId) {
        return outboundMapper.findOutboundByPlanId(outboundPlanId);
    }

    @Override
    public void update(Outbound outbound) {
        outboundMapper.insertOutboundAssign(outbound.getOutboundId(),outbound.getOutboundAssignNumber(),outbound.getOutboundAssignDate());
    }

}
