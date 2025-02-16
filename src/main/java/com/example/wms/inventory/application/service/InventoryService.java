package com.example.wms.inventory.application.service;

import com.example.wms.inventory.application.port.in.InventoryUseCase;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService implements InventoryUseCase {

    private final InventoryPort inventoryPort;

    @Override
    public Page<ProductInfoDto> getAllProductInventories(String productCode, Pageable pageable) {
        Pageable safePageable = convertToSafePageable(pageable);

        List<ProductInfoDto> productInventoryList = inventoryPort.findAllProductInventories(productCode, safePageable);
        long count = inventoryPort.countAllProductInventories(productCode);

        return new PageImpl<>(productInventoryList, safePageable, count);
    }

    private Pageable convertToSafePageable(Pageable pageable) {
        List<Sort.Order> safeOrders = pageable.getSort().stream()
                .filter(order -> List.of("productId", "productCode", "productName", "productCount").contains(order.getProperty()))
                .map(order -> new Sort.Order(
                        order.getDirection(),
                        "productCount".equals(order.getProperty())
                                ? "available_quantity"
                                : camelToSnake(order.getProperty())
                ))
                .collect(Collectors.toList());

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(safeOrders));
    }

    private String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

}
