package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    // outbound 삭제하기
    @Delete("DELETE FROM outbound WHERE outbound_plan_id = #{outboundPlanId}")
    void deleteOutboundById(@Param("outboundPlanId") Long outboundPlanId);

    // outboundPlan 삭제 시 outboundPlanProduct도 함께 삭제
    @Transactional
    default void deleteOutboundPlanAndProducts(Long outboundPlanId) {
        // outbound_plan_product 삭제
        deleteOutboundPlanProductsByPlanId(outboundPlanId);

        // outbound 삭제
        deleteOutboundById(outboundPlanId);

        // outbound_plan 삭제
        deleteById(outboundPlanId);
    }

    // 출고 계획 수정
    @Update("UPDATE outbound_plan SET outbound_schedule_date = #{outboundPlanRequestDto.outboundScheduleDate}, plan_date = #{outboundPlanRequestDto.planDate} WHERE outbound_plan_id = #{outboundPlanId}")
    void updateOutboundPlan(@Param("outboundPlanId") Long outboundPlanId, @Param ("outboundPlanRequestDto") OutboundPlanRequestDto outboundPlanRequestDto);

    // 출고 계획 제품 수정
    @Update({
            "<script>",
            "UPDATE outbound_plan_product",
            "SET required_quantity = CASE",
            "<foreach collection='productList' item='product' index='index' separator=' '>",
            "WHEN product_id = #{product.productId} THEN #{product.productCount}",
            "</foreach>",
            "END",
            "WHERE outbound_plan_id = #{outboundPlanId}",
            "</script>"
    })
    void updateOutboundPlanProducts(@Param("outboundPlanId") Long outboundPlanId, @Param("productList") List<ProductInfoDto> productList);

    List<OutboundPlanResponseDto> findOutboundPlanFilteringWithPageNation(
            @Param("outboundScheduleNumber") String outboundScheduleNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable
    );

    Integer countAllOutboundPlanFiltering(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate);

    @Select("""
        SELECT *
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId};
    """)
    Optional<Outbound> findOutboundByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    @Update("""
        UPDATE outbound_plan
        SET status = #{status}
        WHERE outbound_plan_id = #{outboundPlanId}
    """)
    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);
}
