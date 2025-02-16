package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundPutAwayReqDto;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.port.out.BinPort;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundPutAwayCreateTest {

    @InjectMocks
    private InboundService inboundService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private LotPort lotPort;

    @Mock
    private BinPort binPort;

    @Mock
    private InventoryPort inventoryPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Mock
    private ProductPort productPort;

    @Test
    @DisplayName("입고 적치를 생성하는 경우를 테스트합니다. ")
    public void testCreateInboundPutAway() {

        // given
        Long inboundId = 1L;
        Long productId = 100L;
        Long binId = 10L;
        Integer lotCount = 5;
        String locationBinCode = "A-01-01-01";

        List<InboundPutAwayReqDto> putAwayRequests = List.of(new InboundPutAwayReqDto(productId, lotCount, binId));

        when(productPort.getLocationBinCode(productId)).thenReturn(locationBinCode);

        // when
        inboundService.putAway(inboundId, putAwayRequests);

        verify(inboundPort).updatePA(eq(inboundId), any(LocalDate.class), any());
        verify(lotPort, times(lotCount)).insertLot(any(Lot.class));
        verify(inventoryPort).updateInventory(productId, lotCount);
    }

    @Test
    @DisplayName("binCode가 A-01-01까지 부여되어 있을 때 입고 적치 하는 경우를 테스트합니다.")
    public void testCreateInboundPutAway_WithRowLevelBinCode() {

        // given
        Long inboundId = 1L;
        Long productId = 101L;
        Long binId = 11L;
        Integer lotCount = 3;
        String locationBinCode = "A-01-01";

        List<InboundPutAwayReqDto> putAwayRequests = List.of(new InboundPutAwayReqDto(productId, lotCount, binId));

        when(productPort.getLocationBinCode(productId)).thenReturn(locationBinCode);
        when(binPort.findAvailableBinIdInRow("A",1,1)).thenReturn(binId);

        // when
        inboundService.putAway(inboundId, putAwayRequests);

        // then
        verify(inboundPort).updatePA(eq(inboundId), any(LocalDate.class), any());
        verify(lotPort, times(lotCount)).insertLot(any(Lot.class));
        verify(inventoryPort).updateInventory(productId, lotCount);
    }

    @Test
    @DisplayName("binCode가 A-01까지 부여되어 있을 때 입고 적치하는 경우를 테스트합니다.")
    public void testCreateInboundPutAway_WithAisleLevelBinCode() {

        // given
        Long inboundId = 1L;
        Long productId = 101L;
        Long binId = 11L;
        Integer lotCount = 4;
        String locationBinCode = "A-01";

        List<InboundPutAwayReqDto> putAwayRequests = List.of(new InboundPutAwayReqDto(productId, lotCount, binId));

        when(productPort.getLocationBinCode(productId)).thenReturn(locationBinCode);
        doReturn(binId).when(binPort).findAvailableBinIdInAisle("A", 1);

        // when
        inboundService.putAway(inboundId, putAwayRequests);

        // then
        verify(inboundPort).updatePA(eq(inboundId), any(LocalDate.class),any());
        verify(lotPort, times(lotCount)).insertLot(any(Lot.class));
        verify(inventoryPort).updateInventory(productId, lotCount);
    }

}
