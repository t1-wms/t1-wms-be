package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InboundNumberMapper {

    @Select("SELECT schedule_number FROM inbound ORDER BY schedule_number DESC LIMIT 1")
    String findMaxISNumber();

    @Select("SELECT check_number FROM inbound ORDER BY check_number DESC LIMIT 1")
    String findMaxICNumber();

    @Select("SELECT put_away_number FROM inbound ORDER BY put_away_number DESC LIMIT 1")
    String findMaxPANumber();
}
