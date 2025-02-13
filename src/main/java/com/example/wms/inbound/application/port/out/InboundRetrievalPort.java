package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;

import java.util.List;

public interface InboundRetrievalPort {
    List<InboundResDto> findInboundProductListByOrderId(Long orderId);
}
