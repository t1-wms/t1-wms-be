package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.GetOutboundPlanUseCase;
import com.example.wms.outbound.application.port.out.GetOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOutboundPlanService implements GetOutboundPlanUseCase {

    private final GetOutboundPlanPort getOutboundPlanPort;

    @Override
    public Page<OutboundPlanResponseDto> getOutboundPlans(Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, OutboundPlan.class);

        List<OutboundPlan> outboundPlanList = getOutboundPlanPort.findOutboundPlanWithPagenation(safePageable);
        Integer count = getOutboundPlanPort.countAllOutboundPlan();

        List<OutboundPlanResponseDto> dtoList = outboundPlanList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<OutboundPlanResponseDto> getFilteredOutboundPlans(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, OutboundPlan.class);
        List<OutboundPlan> outboundPlanList = getOutboundPlanPort.findOutboundPlanFilteringWithPageNation(outboundScheduleNumber, startDate, endDate, safePageable);
        Integer count = getOutboundPlanPort.countFilteredOutboundPlan(outboundScheduleNumber, startDate, endDate);
        return new PageImpl<>(convertToDtoList(outboundPlanList), pageable, count);
    }

    private List<OutboundPlanResponseDto> convertToDtoList(List<OutboundPlan> outboundPlanList) {
        return outboundPlanList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private OutboundPlanResponseDto convertToDto(OutboundPlan outboundPlan) {
        List<ProductInfoDto> productList = getOutboundPlanPort.findProductInfoByOutboundPlanId(outboundPlan.getOutboundPlanId())
                .stream()
                .map(p -> ProductInfoDto.builder()
                        .productId(p.getProductId())
                        .productCode(p.getProductCode())
                        .productCount(p.getProductCount())
                        .build())
                .collect(Collectors.toList());

        return OutboundPlanResponseDto.builder()
                .process(outboundPlan.getStatus())  // outbound테이블에서 assign있으면, picking있으면, packing있으면 이걸로 다시 나누기
                .outboundScheduleNumber(outboundPlan.getOutboundScheduleNumber())
                .outboundScheduleDate(outboundPlan.getOutboundScheduleDate())
                .productionPlanNumber(outboundPlan.getProductionPlanNumber())
                .planDate(outboundPlan.getPlanDate())
                .productList(productList)
                .build();
    }
}
