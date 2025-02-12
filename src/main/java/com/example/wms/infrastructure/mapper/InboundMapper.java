package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.application.domain.Inbound;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InboundMapper {
    void insert(Inbound inbound);
}
