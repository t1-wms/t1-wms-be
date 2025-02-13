package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.infrastructure.mapper.InboundRetrievalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InboundRetrievalAdapter implements InboundRetrievalPort {

    private final InboundRetrievalMapper inboundRetrievalMapper;

    @Override
    public List<InboundResDto> findInboundProductListByOrderId(Long orderId) {
        return inboundRetrievalMapper.findInboundProductListByOrderId(orderId);
    }
}
