package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.port.in.DeleteOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundAssignService implements DeleteOutboundAssignUseCase {

    private final DeleteOutboundAssignPort deleteOutboundAssignPort;

    @Override
    public void deleteOutboundAssign(Long outboundId) {
        deleteOutboundAssignPort.deleteOutboundAssign(outboundId);
    }
}
