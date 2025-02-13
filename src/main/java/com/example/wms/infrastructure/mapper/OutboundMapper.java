package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.Outbound;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OutboundMapper {
    // outbound 테이블 생성
    @Insert("""
        INSERT INTO outbound
        (outbound_plan_id, outbound_assign_number, outbound_assign_date, outbound_picking_number, outbound_picking_date, outbound_packing_number, outbound_packing_date, outbound_loading_number, outbound_loading_date)
        VALUES
        (#{outboundPlanId}, #{outboundAssignNumber}, #{outboundAssignDate}, #{outboundPickingNumber}, #{outboundPickingDate}, #{outboundPackingNumber}, #{outboundPackingDate}, #{outboundLoadingNumber}, #{outboundLoadingDate})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "outboundId")
    int insert(Outbound outbound);

    @Select("""
        SELECT outbound_assign_number FROM outbound ORDER BY outbound_assign_number DESC LIMIT 1
    """)
    String findMaxOutboundAssignNumber();

    @Select("""
        SELECT COUNT(*)
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId}
    """)
    int findOutboundAssignByPlanId(@Param("outboundPlanId") Long outboundPlanId);
}
