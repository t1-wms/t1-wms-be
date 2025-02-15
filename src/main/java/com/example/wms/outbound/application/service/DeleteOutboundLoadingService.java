package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.DeleteOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundLoadingService implements DeleteOutboundLoadingUseCase {

    private final DeleteOutboundLoadingPort deleteOutboundLoadingPort;

    @Override
    public void deleteOutboundLoading(Long outboundId) {
        deleteOutboundLoadingPort.deleteOutboundLoading(outboundId);
    }
}
