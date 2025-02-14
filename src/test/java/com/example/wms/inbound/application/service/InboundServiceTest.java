package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundPlanProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.order.application.domain.OrderProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class InboundServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(InboundServiceTest.class);

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<InboundPlanProductDto> mockInboundPlanProductList;

    @BeforeEach
    void setUp() {
        mockInboundPlanProductList = Arrays.asList(
                new InboundPlanProductDto(1L,"입고중",LocalDate.now(),"Schedule1", LocalDateTime.now(), 1L, "Order1",LocalDateTime.now(),1L,"Supplier1",1L,"P001","Product1",10,5),
                new InboundPlanProductDto(2L,"입고완료",LocalDate.now(),"Schedule2", LocalDateTime.now(), 2L, "Order2",LocalDateTime.now(),2L,"Supplier2",2L,"P002","Product2",20,10)
        );
    }

    @Test
    void testGetInboundPlans() {
        Pageable pageable = PageRequest.of(0,10);
        when(inboundRetrievalPort.findInboundProductListWithPagination(any(Pageable.class)))
                .thenReturn(mockInboundPlanProductList);
        when(inboundRetrievalPort.countAllInboundPlan()).thenReturn(mockInboundPlanProductList.size());

        Page<InboundResDto> result = inboundService.getInboundPlans(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockInboundPlanProductList.size());
        assertThat(result.getTotalElements()).isEqualTo(mockInboundPlanProductList.size());

        verify(inboundRetrievalPort,times(1)).findInboundProductListWithPagination(any(Pageable.class));
        verify(inboundRetrievalPort, times(1)).countAllInboundPlan();
    }

    @Test
    void testGetFilteredInboundPlans() {
        // given
        String inboundScheduleNumber = "Schedule1";
        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 2, 10);
        Pageable pageable = PageRequest.of(0, 10);

        when(inboundRetrievalPort.findInboundFilteringWithPagination(eq(inboundScheduleNumber), eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(mockInboundPlanProductList);
        when(inboundRetrievalPort.countFilteredInboundPlan(eq(inboundScheduleNumber), eq(startDate), eq(endDate)))
                .thenReturn(mockInboundPlanProductList.size());

        // when
        Page<InboundResDto> result = inboundService.getFilteredInboundPlans(inboundScheduleNumber, startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockInboundPlanProductList.size());
        assertThat(result.getTotalElements()).isEqualTo(mockInboundPlanProductList.size());

        verify(inboundRetrievalPort, times(1)).findInboundFilteringWithPagination(eq(inboundScheduleNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort, times(1)).countFilteredInboundPlan(eq(inboundScheduleNumber), eq(startDate), eq(endDate));
    }


    @Test
    void getInboundProductListTest() {

        // given
        List<InboundProductDto> productDtoList = List.of(
                new InboundProductDto(1L, "P1001", "카시트", 600, 100),
                new InboundProductDto(2L, "P1002", "유모차", 300, 50),
                new InboundProductDto(3L, "P1003", "침대", 200, 30)
        );

        List<InboundResDto> inboundResDtoList = List.of(
                new InboundResDto(1L, "입고중", LocalDate.now(), "IS202502060000",LocalDateTime.now(),1L,"OD202502060001",LocalDateTime.of(2025,2,6,0,0,20,3),1L,"seat company",productDtoList)
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