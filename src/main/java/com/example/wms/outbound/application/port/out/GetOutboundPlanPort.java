package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetOutboundPlanPort {
    List<OutboundPlan> findOutboundPlanWithPageNation(Pageable pageable);
    List<OutboundPlan> findOutboundPlanFilteringWithPageNation(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countAllOutboundPlan();
    Integer countFilteredOutboundPlan(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate);
    List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId);
}
