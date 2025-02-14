package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.DeleteOutboundPickingUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundPickingService implements DeleteOutboundPickingUseCase {

    private final DeleteOutboundPickingPort deleteOutboundPickingPort;

    @Override
    public void deleteOutboundPicking(Long outboundId) {
        deleteOutboundPickingPort.deleteOutboundPicking(outboundId);
    }
}
