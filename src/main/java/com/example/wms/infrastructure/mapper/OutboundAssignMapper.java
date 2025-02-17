package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundAssignResponseDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutboundAssignMapper {
    // outboundAssign  조회
    List<OutboundAssignResponseDto> findOutboundAssignFilteringWithPageNation(
            @Param("outboundAssignNumber") String outboundAssignNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);
    Integer countAssign(
            @Param("outboundAssignNumber") String outboundAssignNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Update("""
        UPDATE outbound_plan
        SET status = #{status}
        WHERE outbound_plan_id = #{outboundPlanId}
    """)
    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);

    @Select("""
        SELECT *
        FROM outbound
        WHERE outbound_id = #{outboundId};
    """)
    Outbound findOutboundByOutboundId(@Param("outboundId") Long outboundId);
}
