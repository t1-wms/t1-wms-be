package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.DeleteOutboundPackingUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundPackingPort;
import com.example.wms.outbound.application.port.out.DeleteOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundPackingService implements DeleteOutboundPackingUseCase {

    private final DeleteOutboundPackingPort deleteOutboundPackingPort;

    @Override
    public void deleteOutboundPacking(Long outboundId) {
        deleteOutboundPackingPort.deleteOutboundPacking(outboundId);
    }
}
