package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.GetOutboundPickingUseCase;
import com.example.wms.outbound.application.port.out.GetOutboundPickingPort;
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
public class GetOutboundPickingService implements GetOutboundPickingUseCase {

    private final GetOutboundPickingPort getOutboundPickingPort;

    @Override
    public Page<OutboundPickingResponseDto> getFilteredOutboundPickings(String outboundPickingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageale = PageableUtils.convertToSafePageableStrict(pageable, Outbound.class);
        List<Outbound> outboundList = getOutboundPickingPort.findOutboundPickingFilteringWithPageNation(outboundPickingNumber, startDate, endDate, safePageale);
        return new PageImpl<>(covertToDtoList(outboundList), pageable, outboundList.size());
    }

    private List<OutboundPickingResponseDto> covertToDtoList(List<Outbound> outboundList) {
        return outboundList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OutboundPickingResponseDto convertToDto(Outbound outbound) {
        List<ProductInfoDto> productList = getOutboundPickingPort.findProductInfoByOutboundPlanId(outbound.getOutboundPlanId())
                .stream()
                .map(p->ProductInfoDto.builder()
                        .productId(p.getProductId())
                        .productCode(p.getProductCode())
                        .productName(p.getProductName())
                        .productCount(p.getProductCount())
                        .build())
                .collect(Collectors.toList());

        OutboundPlan outboundPlan = getOutboundPickingPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());

        return OutboundPickingResponseDto.builder()
                .outboundId(outbound.getOutboundId())
                .outboundPlanId(outbound.getOutboundPlanId())
                .process("여기수정해야됨") //outbound테이블 보고 수정하기
                .outboundScheduleNumber(outboundPlan.getOutboundScheduleNumber())
                .outboundAssignNumber(outbound.getOutboundAssignNumber())
                .outboundPickingNumber(outbound.getOutboundPickingNumber())
                .outboundPickingDate(outbound.getOutboundAssignDate())
                .productionPlanNumber(outboundPlan.getProductionPlanNumber())
                .planDate(outboundPlan.getPlanDate())
                .productList(productList)
                .build();
    }
}
