package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface OutboundAssignMapper {
    // outboundAssign  조회
    List<Outbound> findOutboundAssignWithPageNation(@Param("pageable") Pageable pageable);
    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);
}
