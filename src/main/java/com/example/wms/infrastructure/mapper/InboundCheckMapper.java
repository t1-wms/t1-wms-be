package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.application.domain.InboundCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InboundCheckMapper {
    void insertInboundCheck(InboundCheck inboundCheck);
    List<InboundCheck> findByInboundId(@Param("inboundId") Long inboundId);
    void insertOrUpdate(InboundCheck inboundCheck);
}
