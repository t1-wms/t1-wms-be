package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundLoadingMapper;
import com.example.wms.outbound.application.port.out.DeleteOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOutboundLoadingAdapter implements DeleteOutboundLoadingPort {

    private final OutboundLoadingMapper outboundLoadingMapper;

    @Override
    public void deleteOutboundLoading(Long outboundId) {
        outboundLoadingMapper.deleteOutboundLoading(outboundId);
    }
}
