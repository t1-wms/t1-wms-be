package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundAssignMapper;
import com.example.wms.infrastructure.mapper.OutboundMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOutboundAssignAdapter implements GetOutboundAssignPort {

    private final OutboundAssignMapper outboundAssignMapper;
    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<Outbound> findOutboundAssignWithPageNation(Pageable pageable) {
        return outboundAssignMapper.findOutboundAssignWithPageNation(pageable);
    }

    @Override
    public List<Outbound> findOutboundAssignFilteringWithPageNation(Pageable pageable) {
        return List.of();
    }

    @Override
    public List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId) {
        return outboundPlanProductMapper.findProductInfoByOutboundPlanId(outboundPlanId);
    }

    @Override
    public OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId) {
        return outboundAssignMapper.findOutboundPlanByOutboundPlanId(outboundPlanId);
    }
}
