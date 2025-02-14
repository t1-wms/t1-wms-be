package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.domain.Inbound;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InboundMapper {
    void insert(Inbound inbound);
    List<InboundResDto> findInboundProductListByOrderId(Long orderId);
    void delete(Long inboundId);
}
