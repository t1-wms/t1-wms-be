package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundCreateTest {

    @Mock
    private InboundPort inboundPort;

    @Mock
    private AssignInboundNumberPort inboundNumberPort;

    @Mock
    private InboundService inboundService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 승인된 주문 조회
    // order 정보 중
//    {
//        delivery_deadLine: String
//        supplierId: Long
//        orderId: Long
//        orderNumber: String
//        orderDate: String 이 값들만 사용 -> dto를 사용?
//    }

    // 재고가 부족해서 wms에서 발주 신청을 하면 납품업체에서
    // 승인하는 경우 납품기한을 보냄  (=승인날짜 + 리드 타임)
    // 그러면 입하 예정이 생성됨 (schedule Date = 납품기한임)
    // inbound schedule Date <- InboundDate 로 저장함

     @Test
     @DisplayName("inbound 저장 테스트")
     void makeScheduleInbound() {

        // given
        LocalDate date = LocalDate.of(2025,3,25);

        InboundReqDto inboundReqDto = InboundReqDto.builder()
                .scheduleDate(date)
                .orderId(1L)
                .build();

        doNothing().when(inboundService).createInboundPlan(inboundReqDto);

        // when
        inboundService.createInboundPlan(inboundReqDto);

        // then
        verify(inboundService, times(1)).createInboundPlan(inboundReqDto);
     }


}
