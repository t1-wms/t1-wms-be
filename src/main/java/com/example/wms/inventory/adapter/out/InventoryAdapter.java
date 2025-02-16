package com.example.wms.inventory.adapter.out;

import com.example.wms.infrastructure.mapper.InventoryMapper;
import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryAdapter implements InventoryPort {

    private final InventoryMapper inventoryMapper;

    @Override
    public List<ProductInfoDto> findAllProductInventories(String productCode, Pageable pageable) {
        return inventoryMapper.findAllProductInventories(productCode, pageable);
    }

    @Override
    public long countAllProductInventories(String productCode) {
        return inventoryMapper.countAllProductInventories(productCode);
    }

    @Override
    public List<ProductThresholdDto> findAllProductThresholds(String productCode, Pageable pageable) {
        return inventoryMapper.findAllProductThresholds(productCode, pageable);
    }
}
