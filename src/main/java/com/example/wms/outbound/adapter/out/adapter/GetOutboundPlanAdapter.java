package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.GetOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOutboundPlanAdapter implements GetOutboundPlanPort {

    private final OutboundPlanMapper outboundPlanMapper;
    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<OutboundPlan> findOutboundPlanWithPageNation(Pageable pageable) {
        return outboundPlanMapper.findOutboundPlanWithPageNation(pageable);
    }

    @Override
    public List<OutboundPlan> findOutboundPlanFilteringWithPageNation(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return outboundPlanMapper.findOutboundPlanFilteringWithPageNation(outboundScheduleNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countAllOutboundPlan() {
        return outboundPlanMapper.countAllOutboundPlan();
    }

    @Override
    public Integer countFilteredOutboundPlan(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate) {
        return outboundPlanMapper.countAllOutboundPlanFiltering(outboundScheduleNumber,startDate,endDate);
    }

    @Override
    public List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId) {
        return outboundPlanProductMapper.findProductInfoByOutboundPlanId(outboundPlanId);
    }
}
