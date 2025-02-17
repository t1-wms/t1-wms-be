package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class InboundGetTest {

    private static final Logger logger = LoggerFactory.getLogger(InboundGetTest.class);

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<InboundAllProductDto> mockInboundPlanProductList;

    @BeforeEach
    void setUp() {
        mockInboundPlanProductList = Arrays.asList(
                new InboundAllProductDto(1L,"입고중",LocalDate.now(),"Schedule1", LocalDate.now(), 1L, "Order1", LocalDateTime.now(),1L,"Supplier1",1L,"P001","Product1",10,5),
                new InboundAllProductDto(2L,"입고완료",LocalDate.now(),"Schedule2", LocalDate.now(), 2L, "Order2", LocalDateTime.now(),2L,"Supplier2",2L,"P002","Product2",20,10)
        );
    }

    @Test
    @DisplayName("입하 예정 목록을 조회합니다.")
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
    @DisplayName("입하 예정 목록을 필터링하여 조회합니다.")
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


}