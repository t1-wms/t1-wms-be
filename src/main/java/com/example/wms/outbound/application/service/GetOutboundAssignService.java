package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.OutboundAssignResponseDto;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.GetOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
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
public class GetOutboundAssignService implements GetOutboundAssignUseCase {

    private final GetOutboundAssignPort getOutboundAssignPort;

    @Override
    public Page<OutboundAssignResponseDto> getFilteredOutboundAssings(String outboundAssignNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Outbound.class);
        List<Outbound> outboundList = getOutboundAssignPort.findOutboundAssignFilteringWithPageNation(outboundAssignNumber, startDate, endDate, safePageable);
        return new PageImpl<>(covertToDtoList(outboundList), pageable, outboundList.size());
    }

    private List<OutboundAssignResponseDto> covertToDtoList(List<Outbound> outboundList) {
        return outboundList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OutboundAssignResponseDto convertToDto(Outbound outbound) {
        List<ProductInfoDto> productList = getOutboundAssignPort.findProductInfoByOutboundPlanId(outbound.getOutboundPlanId())
                .stream()
                .map(p->ProductInfoDto.builder()
                        .productId(p.getProductId())
                        .productCode(p.getProductCode())
                        .productName(p.getProductName())
                        .productCount(p.getProductCount())
                        .build())
                .collect(Collectors.toList());

        OutboundPlan outboundPlan = getOutboundAssignPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());
        
        return OutboundAssignResponseDto.builder()
                .outboundId(outbound.getOutboundId())
                .outboundPlanId(outbound.getOutboundPlanId())
                .process("여기수정해야됨") //outbound테이블 보고 수정하기
                .outboundScheduleNumber(outboundPlan.getOutboundScheduleNumber())
                .outboundAssignNumber(outbound.getOutboundAssignNumber())
                .outboundAssignDate(outbound.getOutboundAssignDate())
                .productionPlanNumber(outboundPlan.getProductionPlanNumber())
                .planDate(outboundPlan.getPlanDate())
                .productList(productList)
                .build();
    }
}
