package com.example.wms.inventory.application.port.in;

import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryUseCase {

    Page<ProductInfoDto> getAllProductInventories(String productCode, Pageable pageable);
    Page<ProductThresholdDto> getAllProductThresholds(String productCode, Pageable pageable);
}
