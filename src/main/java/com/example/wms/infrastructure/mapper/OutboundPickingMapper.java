package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.Outbound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

@Mapper
public interface OutboundPickingMapper {

    @Update("""
    UPDATE outbound
    SET outbound_picking_number = #{outboundPickingNumber},
        outbound_picking_date = #{outboundPickingDate}
    WHERE outbound_id = #{outboundId}
    """)
    void insertOutboundPicking(
            @Param("outboundId") Long outboundId,
            @Param("outboundPickingNumber") String outboundPickingNumber,
            @Param("outboundPickingDate") LocalDate outboundPickingDate
    );

    @Select("""
        SELECT outbound_picking_number FROM outbound ORDER BY outbound_picking_number DESC LIMIT 1
    """)
    String findMaxOutboundPickingNumber();

    @Select("""
        SELECT * 
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId};
    """)
    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // 출고 피킹 삭제
    @Update("UPDATE outbound SET outbound_picking_date = NULL, outbound_picking_number = NULL WHERE outbound_id = #{outboundId}")
    void deleteOutboundPicking(@Param("outboundId") Long outboundId);

    // 출고 피킹 수정
    @Update("UPDATE outbound SET outbound_picking_date = #{outboundPickingDate} WHERE outbound_id = #{outboundId}")
    void updateOutboundPicking(@Param("outboundId") Long outboundId, @Param("outboundPickingDate") LocalDate outboundPickingDate);

}
