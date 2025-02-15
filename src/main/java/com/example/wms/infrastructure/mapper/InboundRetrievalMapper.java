package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundRetrievalMapper {
    List<InboundProductDto> findInboundProductListByOrderId(Long orderId);

    List<InboundAllProductDto> findInboundProductListWithPagination(@Param("pageable") Pageable pageable);

    List<InboundAllProductDto> findInboundFilteringWithPagination(@Param("inboundScheduleNumber") String inboundScheduleNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);

    Integer countAllInboundPlan();

    Integer countAllInboundPlanFiltering(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate);

    Integer countAllInboundCheckFiltering(String inboundCheckNumber, LocalDate startDate, LocalDate endDate);
}