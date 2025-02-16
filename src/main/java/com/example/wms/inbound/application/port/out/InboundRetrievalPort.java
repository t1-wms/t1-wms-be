package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundRetrievalPort {
    List<InboundProductDto>  findInboundProductListByOrderId(Long orderId);
    List<InboundAllProductDto> findInboundProductListWithPagination(Pageable pageable);
    List<InboundAllProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countAllInboundPlan();
    Integer countFilteredInboundPlan(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate);
    Integer countFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate);
    List<InboundPutAwayResDto> findFilteredInboundPutAway(String putAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countFilteredPutAway(String putAwayNumber, LocalDate startDate, LocalDate endDate);
}
