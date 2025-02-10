package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundUseCase;
import com.example.wms.outbound.application.port.out.CalculateOsNumberPort;
import com.example.wms.outbound.application.port.out.CreateOutboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateOutboundService implements CreateOutboundUseCase {
    private final CreateOutboundPort createOutboundPort;
    private final CalculateOsNumberPort calculateOsNumberPort;

    @Transactional
    @Override
    public void createOutbound(OutboundRequestDto outboundRequestDto) {

        String currentDate = LocalDate.now().toString().replace("-", ""); // 예: 2025-02-10 -> 20250210

        // DB에서 가장 큰 outboundScheduleNumber 조회
        String maxOutboundNumber = calculateOsNumberPort.findMaxOutboundScheduleNumber();
        String nextNumber = "0000";

        if (maxOutboundNumber != null) {
            // 마지막 4자리 숫자 추출해서 + 1
            String lastNumberStr = maxOutboundNumber.substring(maxOutboundNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String osNumber = "OS" + currentDate + nextNumber;

        // 엔티티로 변환하기
        OutboundPlan outboundPlan = OutboundPlan.builder()
                .planDate(outboundRequestDto.getPlanDate())
                .status("진행중")
                .outboundScheduleNumber(osNumber)
                .outboundScheduleDate(outboundRequestDto.getOutboundScheduleDate())
                .productionPlanNumber(outboundRequestDto.getProductionPlanId())
                .build();

        // DB에 저장하기
        createOutboundPort.save(outboundPlan);
    }
}
