package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CalculateOsNumberMapper {
    @Select("SELECT outbound_schedule_number FROM outbound_plan ORDER BY outbound_schedule_number DESC LIMIT 1")
    String findMaxOutboundScheduleNumber(); // 가장 큰 outboundScheduleNumber 조회
}
