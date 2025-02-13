package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;

import java.util.List;

public interface InboundRetrievalMapper {
    List<InboundResDto> findInboundProductListByOrderId(Long orderId);
}
