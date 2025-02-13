package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.order.application.domain.OrderProduct;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class InboundServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(InboundServiceTest.class);

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    @Test
    void getInboundProductListTest() {

        // given
        List<InboundProductDto> productDtoList = List.of(
                new InboundProductDto(1L, "P1001", "카시트", 600, 100),
                new InboundProductDto(2L, "P1002", "유모차", 300, 50),
                new InboundProductDto(3L, "P1003", "침대", 200, 30)
        );
        
        List<InboundResDto> inboundResDtoList = List.of(
                new InboundResDto(1L, "입고중", LocalDateTime.now(), "IS202502060000",LocalDateTime.now(),1L,"OD202502060001",LocalDateTime.of(2025,2,6,0,0,20,3),1L,"seat company",productDtoList)
        );

        OrderProduct orderProduct = OrderProduct.builder()
                .orderProductId(1L)
                .orderId(1L)
                .productId(1L)
                .isDefective(false)
                .binId(1L)
                .build();

        Long orderId = 1L;

        when(inboundRetrievalPort.findInboundProductListByOrderId(orderId)).thenReturn(inboundResDtoList);

        // when
        List<InboundResDto> inboundResDtos = inboundService.getAllInboundProductList(orderProduct);

        // then
        assertThat(inboundResDtos).isNotNull();
        assertThat(inboundResDtos.size()).isEqualTo(1);
        assertThat(inboundResDtos.get(0).getInboundId()).isEqualTo(1L);
        assertThat(inboundResDtos.get(0).getInboundStatus()).isEqualTo("입고중");
        assertThat(inboundResDtos.get(0).getScheduleNumber()).isEqualTo("IS202502060000");
        assertThat(inboundResDtos.get(0).getOrderNumber()).isEqualTo("OD202502060001");
        assertThat(inboundResDtos.get(0).getSupplierName()).isEqualTo("seat company");
        assertThat(inboundResDtos.get(0).getProductList().size()).isEqualTo(3);
        assertThat(inboundResDtos.get(0).getProductList().get(0).getProductId()).isEqualTo(1L);
        assertThat(inboundResDtos.get(0).getProductList().get(0).getProductCode()).isEqualTo("P1001");
        assertThat(inboundResDtos.get(0).getProductList().get(0).getProductName()).isEqualTo("카시트");

        inboundResDtos.forEach(dto->logger.info(dto.toString()));
    }
}