package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("출고 관련 테스트1")
class CreateOutboundServiceTest {

    //의존성 모킹하기
    @Mock
    private CreateOutboundPort createOutboundPort;

    //테스트 대상임
    @InjectMocks
    private CreateOutboundService createOutboundService;

    private OutboundRequestDto outboundRequestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화하기
        // given
        outboundRequestDto = OutboundRequestDto.builder()
                .outboundScheduleDate(LocalDate.of(2025, 2, 10))
                .planDate(LocalDate.of(2025, 2, 15))
                .productionPlanId("주문번호테스트")
                .build();
    }

    @Test
    @DisplayName("outboundPlan 잘 저장되는지 확인하는 테스트")
    void testCreateOutbound() {
        // when 저장하기
        createOutboundService.createOutbound(outboundRequestDto);

        // then
        OutboundPlan expectedOutboundPlan = OutboundPlan.builder()
                .outboundScheduleDate(outboundRequestDto.getOutboundScheduleDate())
                .planDate(outboundRequestDto.getPlanDate())
                .productionPlanNumber(outboundRequestDto.getProductionPlanId())
                .build();

        assertThat(expectedOutboundPlan).isEqualToComparingFieldByField(OutboundPlan.builder()
                .outboundScheduleDate(outboundRequestDto.getOutboundScheduleDate())
                .planDate(outboundRequestDto.getPlanDate())
                .productionPlanNumber(outboundRequestDto.getProductionPlanId())
                .build());
    }
}