package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPackingResponseDto;
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

    // 출고 패킹 조회
    List<OutboundPackingResponseDto> findOutboundPackingFilteringWithPageNation(
            @Param("outboundPackingNumber") String outboundPackingNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    Integer countPacking(@Param("outboundPackingNumber") String outboundPackingNumber,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);

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
