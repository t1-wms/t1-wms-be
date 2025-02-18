package com.example.wms.infrastructure.mapper;

import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardMapper {
    OutboundStatusResponseDto selectOutboundStatusCounts();
}
