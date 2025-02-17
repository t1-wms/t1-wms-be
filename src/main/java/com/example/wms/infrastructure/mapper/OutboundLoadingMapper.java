package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundAssignResponseDto;
import com.example.wms.outbound.adapter.in.dto.OutboundLoadingResponseDto;
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
public interface OutboundLoadingMapper {
    // 출고 로딩 등록
    @Update("""
    UPDATE outbound
    SET outbound_loading_number = #{outboundLoadingNumber},
        outbound_loading_date = #{outboundLoadingDate}
    WHERE outbound_id = #{outboundId}
    """)
    void insertOutboundLoading(
            @Param("outboundId") Long outboundId,
            @Param("outboundLoadingNumber") String outboundLoadingNumber,
            @Param("outboundLoadingDate") LocalDate outboundLoadingDate
    );

    @Select("""
        SELECT outbound_loading_number FROM outbound ORDER BY outbound_loading_number DESC LIMIT 1
    """)
    String findMaxOutboundLoadingNumber();

    @Select("""
        SELECT * 
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId};
    """)
    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // 출고 로딩 삭제
    @Update("UPDATE outbound SET outbound_loading_date = NULL, outbound_loading_number = NULL WHERE outbound_id = #{outboundId}")
    void deleteOutboundLoading(@Param("outboundId") Long outboundId);

    // 출고 로딩 수정
    @Update("UPDATE outbound SET outbound_loading_date = #{outboundLoadingDate} WHERE outbound_id = #{outboundId}")
    void updateOutboundLoading(@Param("outboundId") Long outboundId, @Param("outboundLoadingDate") LocalDate outboundLoadingDate);

    // 출고 로딩 조회
    List<OutboundLoadingResponseDto> findOutboundLoadingFilteringWithPageNation(
            @Param("outboundLoadingNumber") String outboundLoadingNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    @Select("""
        SELECT *
        FROM outbound
        WHERE outbound_id = #{outboundId};
    """)
    Outbound findOutboundByOutboundId(@Param("outboundId") Long outboundId);

    Integer countLoading(@Param("outboundLoadingNumber") String outboundLoadingNumber,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);

    @Update("""
        UPDATE outbound_plan
        SET status = #{status}
        WHERE outbound_plan_id = #{outboundPlanId}
    """)
    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);
}
