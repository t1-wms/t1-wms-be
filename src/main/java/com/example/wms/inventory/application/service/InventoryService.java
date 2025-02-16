package com.example.wms.inventory.application.service;

import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.adapter.in.dto.ThresholdUpdateRequestDto;
import com.example.wms.inventory.application.port.in.InventoryUseCase;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.product.application.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService implements InventoryUseCase {

    private final InventoryPort inventoryPort;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductInfoDto> getAllProductInventories(String productCode, Pageable pageable) {
        List<String> allowedFields = List.of("productId", "productCode", "productName", "productCount");
        Pageable safePageable = convertToSafePageable(pageable, allowedFields);

        List<ProductInfoDto> productInventoryList = inventoryPort.findAllProductInventories(productCode, safePageable);
        long count = inventoryPort.countAllProductInventories(productCode);

        return new PageImpl<>(productInventoryList, safePageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductThresholdDto> getAllProductThresholds(String productCode, Pageable pageable) {
        List<String> allowedFields = List.of("productId", "productCode", "productName", "productCount", "productThreshold");
        Pageable safePageable = convertToSafePageable(pageable, allowedFields);

        List<ProductThresholdDto> productThresholdDtoList = inventoryPort.findAllProductThresholds(productCode, safePageable);
        long count = inventoryPort.countAllProductInventories(productCode);

        return new PageImpl<>(productThresholdDtoList, safePageable, count);
    }

    @Override
    @Transactional
    public Product updateThreshold(ThresholdUpdateRequestDto thresholdUpdateRequestDto) {
        int updatedCount = inventoryPort.updateThreshold(thresholdUpdateRequestDto);
        Product existingProduct = inventoryPort.findByProductId(thresholdUpdateRequestDto.getProductId());
        return existingProduct;
    }

    private Pageable convertToSafePageable(Pageable pageable, List<String> allowedProperties) {
        pageable.getSort().forEach(order -> {
            if (!allowedProperties.contains(order.getProperty())) {
                throw new IllegalArgumentException("허용되지 않은 정렬 조건이 있습니다: " + order.getProperty());
            }
        });
        List<Sort.Order> safeOrders = pageable.getSort().stream()
                .filter(order -> allowedProperties.contains(order.getProperty()))
                .map(order -> {
                    String property;
                    if ("productCount".equals(order.getProperty())) {
                        property = "available_quantity";
                    } else if ("productThreshold".equals(order.getProperty())) {
                        property = "threshold";
                    } else {
                        property = camelToSnake(order.getProperty());
                    }
                    return new Sort.Order(order.getDirection(), property);
                })
                .collect(Collectors.toList());

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(safeOrders));
    }


    private String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

}
