package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundPlanProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundRetrievalMapper {
    List<InboundProductDto> findInboundProductListByOrderId(Long orderId);

    List<InboundPlanProductDto> findInboundProductListWithPagination(@Param("pageable") Pageable pageable);

    List<InboundPlanProductDto> findInboundFilteringWithPagination(@Param("inboundScheduleNumber") String inboundScheduleNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);

    Integer countAllInboundPlan();

    Integer countAllInboundPlanFiltering(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate);

}