package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundPlanProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.domain.Inbound;
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
    public List<InboundResDto> findInboundProductListByOrderId(Long orderId) {
        return inboundRetrievalMapper.findInboundProductListByOrderId(orderId);
    }

    @Override
    public List<InboundPlanProductDto> findInboundProductListWithPagination(Pageable pageable) {
        return inboundRetrievalMapper.findInboundProductListWithPagination(pageable);
    }

    @Override
    public List<InboundPlanProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
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
}
