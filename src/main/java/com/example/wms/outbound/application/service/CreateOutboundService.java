package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOutboundService implements CreateOutboundUseCase {
    private final CreateOutboundPort createOutboundPort;

    @Transactional
    @Override
    public void createOutbound(OutboundRequestDto outboundRequestDto) {
        // 엔티티로 변환하기
        OutboundPlan outboundPlan = OutboundPlan.builder()
                .outboundScheduleDate(outboundRequestDto.getOutboundScheduleDate())
                .planDate(outboundRequestDto.getPlanDate())
                .productionPlanNumber(outboundRequestDto.getProductionPlanId())
                .build();

        // DB에 저장하기
        createOutboundPort.save(outboundPlan);
    }
}
