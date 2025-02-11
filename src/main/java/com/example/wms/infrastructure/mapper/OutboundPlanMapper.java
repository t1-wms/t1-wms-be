package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface OutboundPlanMapper {
    
    // outboundPlan 저장하기
    @Insert("""
        INSERT INTO outbound_plan 
        (plan_date, status, outbound_schedule_number, outbound_schedule_date, production_plan_number) 
        VALUES 
        (#{planDate}, #{status}, #{outboundScheduleNumber}, #{outboundScheduleDate}, #{productionPlanNumber})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "outboundPlanId") //자동증가
    void insert(OutboundPlan outboundPlan);

    // outboundPlan 삭제하기
    @Delete("DELETE FROM outbound_plan WHERE outbound_plan_id = #{outboundPlanId}")
    void deleteById(@Param("outboundPlanId") Long outboundPlanId);

    // outboundPlanProduct 삭제하기
    @Delete("DELETE FROM outbound_plan_product WHERE outbound_plan_id = #{outboundPlanId}")
    void deleteOutboundPlanProductsByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // outboundPlan 삭제 시 outboundPlanProduct도 함께 삭제
    @Transactional
    default void deleteOutboundPlanAndProducts(Long outboundPlanId) {
        // 1. outbound_plan_product 삭제
        deleteOutboundPlanProductsByPlanId(outboundPlanId);

        // 2. outbound_plan 삭제
        deleteById(outboundPlanId);
    }
}
