package com.example.wms;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.order.adapter.in.dto.response.OrderApproveResDto;
import com.example.wms.order.application.port.out.OrderQueryPort;
import com.example.wms.order.application.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.Month.FEBRUARY;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ScheduledInboundCreate {

    @Mock
    private OrderQueryPort orderQueryPort;

    @InjectMocks
    private OrderService orderService;

    private OrderApproveResDto orderApproveResDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        orderApproveResDto = OrderApproveResDto.builder()
                .orderId(1L)
                .supplierId(1L)
                .orderNumber("ORD-20250211-001")
                .deliveryDeadLine(LocalDate.of(2025,2,26))
                .orderDate(LocalDateTime.of(2025,2,14,4,30))
                .build();
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
    // order에 delivery_deadLine이 납품기한임
    // 그러면 입하 예정이 생성됨 (schedule Date = 납품기한임)
    // inbound schedule Date <- delivery_deadLine 으로 저장함

     @Test
     void getApprovedOrdersTest() {
         List<OrderApproveResDto> orders = orderService.getApprovedOrders();
         assertNotNull(orders, "승인된 발주 데이터가 있어야 합니다.");
     }

     @Test
     void makeScheduleInbound() {
        LocalDate date = orderApproveResDto.getDeliveryDeadLine();
        Long orderId = orderApproveResDto.getOrderId();
        assertThat(date.getMonth()).isEqualTo(FEBRUARY);
        assertThat(orderId).isEqualTo(1L);
        assertThat(date.getYear()).isEqualTo(2025);
        Inbound inbound = new Inbound();
        inbound.setOrderId(orderId);
        inbound.setScheduleDate(date);
        assertThat(inbound.getOrderId()).isEqualTo(orderId);
        assertThat(inbound.getScheduleDate()).isEqualTo(date);
     }


}
