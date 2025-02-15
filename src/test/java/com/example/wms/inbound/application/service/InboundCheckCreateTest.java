package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.domain.InboundCheck;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundCheckPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundCheckCreateTest {

    @InjectMocks
    private InboundService inboundService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Mock
    private LotPort lotPort;

    @Mock
    private InboundCheckPort inboundCheckPort;

    @Mock
    private OrderPort orderPort;

    @Mock
    private ProductPort productPort;

    @Test
    @DisplayName("관리자가 입하 검사를 생성하면 InboundCheckPort의 update 메서드와 LotPort의 updateStatus가 호출되어야 한다.")
    public void testCreateInboundInspection() {

        // given
        InboundCheckedProductReqDto checkedProductDto1 = InboundCheckedProductReqDto.builder()
                .productId(1L) // 품목 ID
                .defectiveLotCount(15L) // 불합격 lot 개수
                .build();

        InboundCheckedProductReqDto checkedProductDto2 = InboundCheckedProductReqDto.builder()
                .productId(2L) // 품목 ID
                .defectiveLotCount(20L) // 불합격 lot 개수
                .build();

        List<InboundCheckedProductReqDto> inboundCheckedProductDtoList = Arrays.asList(checkedProductDto1, checkedProductDto2); // 수정 필요

        // given
        InboundCheckReqDto inboundCheckReqDto = InboundCheckReqDto.builder()
                .inboundId(1L) // inboundId
                .checkDate(LocalDate.now()) // 입하 검사 일자
                .scheduleNumber("IS202502060001") // 입하 예정 번호
                .checkedProductList(inboundCheckedProductDtoList) // 검사 결과
                .build();

        Inbound inbound = Inbound.builder()
                        .inboundId(1L)
                        .scheduleNumber("IS202502060001")// 입하 예정 번호
                        .checkDate(inboundCheckReqDto.getCheckDate())
                        .checkNumber("IC202502060001") // 입하 검사 번호
                        .build();

        Product product1 = Product.builder()
                        .productId(1L)
                        .build();

        Product product2 = Product.builder()
                        .productId(2L)
                        .build();

        when(inboundPort.findById(1L)).thenReturn(inbound);
        when(productPort.findById(1L)).thenReturn(product1);
        when(productPort.findById(2L)).thenReturn(product2);

        // when
        inboundService.createInboundCheck(inboundCheckReqDto);

        // then
        verify(inboundPort, times(1)).updateIC(eq(1L), any(LocalDate.class), any(String.class));
        verify(lotPort, times(1)).updateStatus(1L);
        verify(lotPort, times(1)).updateStatus(2L);

        verify(inboundCheckPort, times(2)).save(any(InboundCheck.class));

    }
}
