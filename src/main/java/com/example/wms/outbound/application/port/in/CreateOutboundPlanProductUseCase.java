package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;

import java.util.List;

public interface CreateOutboundPlanProductUseCase {
    void createOutboundPlanProduct(Long outboundPlanId,List<ProductInfoDto> productInfoDtoList);
}
