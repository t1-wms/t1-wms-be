package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundPlanProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundRetrievalPort {
    List<InboundProductDto>  findInboundProductListByOrderId(Long orderId);
    List<InboundPlanProductDto> findInboundProductListWithPagination(Pageable pageable);
    List<InboundPlanProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countAllInboundPlan();
    Integer countFilteredInboundPlan(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate);
}
