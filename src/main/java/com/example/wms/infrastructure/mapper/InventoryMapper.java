package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InventoryMapper {
    List<ProductInfoDto> findAllProductInventories(@Param("productCode") String productCode, @Param("pageable")Pageable pageable);
    long countAllProductInventories(@Param("productCode") String productCode);
    void updateInventory(Long productId, Integer lotCount);
}
