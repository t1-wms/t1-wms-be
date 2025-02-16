package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import com.example.wms.inbound.adapter.in.dto.response.ProductInboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.infrastructure.mapper.InboundRetrievalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InboundRetrievalAdapter implements InboundRetrievalPort {

    private final InboundRetrievalMapper inboundRetrievalMapper;

    @Override
    public List<InboundProductDto> findInboundProductListByOrderId(Long orderId) {
        return inboundRetrievalMapper.findInboundProductListByOrderId(orderId);
    }

    @Override
    public List<InboundAllProductDto> findInboundProductListWithPagination(Pageable pageable) {
        return inboundRetrievalMapper.findInboundProductListWithPagination(pageable);
    }

    @Override
    public List<InboundAllProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundRetrievalMapper.findInboundFilteringWithPagination(inboundScheduleNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countAllInboundPlan() {
        return inboundRetrievalMapper.countAllInboundPlan();
    }

    @Override
    public Integer countFilteredInboundPlan(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate) {
        return inboundRetrievalMapper.countAllInboundPlanFiltering(inboundScheduleNumber, startDate, endDate);
    }

    @Override
    public Integer countFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate) {
        return inboundRetrievalMapper.countAllInboundCheckFiltering(inboundCheckNumber, startDate, endDate);
    }

    @Override
    public List<InboundPutAwayResDto> findFilteredInboundPutAway(String putAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundRetrievalMapper.findInboundPutAwayFilteringWithPagination(putAwayNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countFilteredPutAway(String putAwayNumber, LocalDate startDate, LocalDate endDate) {
        return inboundRetrievalMapper.countFilteredPutAway(putAwayNumber, startDate, endDate);
    }

    @Override
    public List<ProductInboundResDto> findAllInboundByProductWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundRetrievalMapper.findAllInboundByProductWithPagination(startDate, endDate, pageable);
    }


}
