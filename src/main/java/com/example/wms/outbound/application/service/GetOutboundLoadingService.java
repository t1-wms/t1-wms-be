package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.OutboundLoadingResponseDto;
import com.example.wms.outbound.adapter.in.dto.OutboundPackingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.GetOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.out.GetOutboundLoadingPort;
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
public class GetOutboundLoadingService implements GetOutboundLoadingUseCase {

    private final GetOutboundLoadingPort getOutboundLoadingPort;

    @Override
    public Page<OutboundLoadingResponseDto> getFilteredOutboundLoadings(String outboundLoadingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageale = PageableUtils.convertToSafePageableStrict(pageable, Outbound.class);
        List<Outbound> outboundList = getOutboundLoadingPort.findOutboundLoadingFilteringWithPageNation(outboundLoadingNumber, startDate, endDate, safePageale);
        return new PageImpl<>(covertToDtoList(outboundList), pageable, outboundList.size());
    }

    private List<OutboundLoadingResponseDto> covertToDtoList(List<Outbound> outboundList) {
        return outboundList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OutboundLoadingResponseDto convertToDto(Outbound outbound) {
        List<ProductInfoDto> productList = getOutboundLoadingPort.findProductInfoByOutboundPlanId(outbound.getOutboundPlanId())
                .stream()
                .map(p->ProductInfoDto.builder()
                        .productId(p.getProductId())
                        .productCode(p.getProductCode())
                        .productName(p.getProductName())
                        .productCount(p.getProductCount())
                        .build())
                .collect(Collectors.toList());

        OutboundPlan outboundPlan = getOutboundLoadingPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());

        return OutboundLoadingResponseDto.builder()
                .outboundId(outbound.getOutboundId())
                .outboundPlanId(outbound.getOutboundPlanId())
                .process("여기수정해야됨") //outbound테이블 보고 수정하기
                .outboundScheduleNumber(outboundPlan.getOutboundScheduleNumber())
                .outboundAssignNumber(outbound.getOutboundAssignNumber())
                .outboundPickingNumber(outbound.getOutboundPickingNumber())
                .outboundPackingNumber(outbound.getOutboundPackingNumber())
                .outboundLoadingNumber(outbound.getOutboundLoadingNumber())
                .outboundLoadingDate(outbound.getOutboundLoadingDate())
                .productionPlanNumber(outboundPlan.getProductionPlanNumber())
                .planDate(outboundPlan.getPlanDate())
                .productList(productList)
                .build();
    }
}
