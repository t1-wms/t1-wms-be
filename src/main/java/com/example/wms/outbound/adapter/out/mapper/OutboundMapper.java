package com.example.wms.outbound.adapter.out.mapper;

import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface OutboundMapper {
    
    // outboundPlan저장
    @Insert("""
        INSERT INTO outbound_plan 
        (plan_date, status, outbound_schedule_number, outbound_schedule_date, production_plan_number) 
        VALUES 
        (#{planDate}, #{status}, #{outboundScheduleNumber}, #{outboundScheduleDate}, #{productionPlanNumber})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "outboundPlanId") //자동증가
    void insert(OutboundPlan outboundPlan);
}
