package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.Outbound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

@Mapper
public interface OutboundPackingMapper {
    @Update("""
    UPDATE outbound
    SET outbound_packing_number = #{outboundPackingNumber},
        outbound_packing_date = #{outboundPackingDate}
    WHERE outbound_id = #{outboundId}
    """)
    void insertOutboundPacking(
            @Param("outboundId") Long outboundId,
            @Param("outboundPackingNumber") String outboundPackingNumber,
            @Param("outboundPackingDate") LocalDate outboundPackingDate
    );

    @Select("""
        SELECT outbound_packing_number FROM outbound ORDER BY outbound_packing_number DESC LIMIT 1
    """)
    String findMaxOutboundPackingNumber();

    @Select("""
        SELECT * 
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId};
    """)
    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // 출고 피킹 삭제
    @Update("UPDATE outbound SET outbound_packing_date = NULL, outbound_packing_number = NULL WHERE outbound_id = #{outboundId}")
    void deleteOutboundPacking(@Param("outboundId") Long outboundId);

    // 출고 피킹 수정
    @Update("UPDATE outbound SET outbound_packing_date = #{outboundPackingDate} WHERE outbound_id = #{outboundId}")
    void updateOutboundPacking(@Param("outboundId") Long outboundId, @Param("outboundPackingDate") LocalDate outboundPackingDate);

}
