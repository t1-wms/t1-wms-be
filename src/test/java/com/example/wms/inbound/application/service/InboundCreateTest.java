package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.order.application.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InboundCreateTest {

    @InjectMocks
    private InboundService inboundService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Test
    @DisplayName("입하 스케줄을 생성하면 InboundPort의 save 메서드가 호출되어야 한다.")
    public void testCreateInboundSchedule() {

        // given
        Order order = Order.builder()
                .orderId(1L)
                .supplierId(1L)
                .inboundDate(LocalDateTime.of(2025,2,14,10,0))
                .build();

        inboundService.createInboundSchedule(order);

        verify(inboundPort, times(1)).save(any(Inbound.class));
    }
}
