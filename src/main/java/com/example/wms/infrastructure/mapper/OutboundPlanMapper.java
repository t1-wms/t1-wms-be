package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
        // outbound_plan_product 삭제
        deleteOutboundPlanProductsByPlanId(outboundPlanId);

        // outbound_plan 삭제
        deleteById(outboundPlanId);
    }

    // outboundPlan 조회하기
    List<OutboundPlan> findOutboundPlanWithPageNation(@Param("pageable") Pageable pageable);
    Integer countAllOutboundPlan();

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

    List<OutboundPlan> findOutboundPlanFilteringWithPageNation(
            @Param("outboundScheduleNumber") String outboundScheduleNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable
    );

    Integer countAllOutboundPlanFiltering(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate);
}
