package com.example.wms.infrastructure.mapper;

import com.example.wms.order.adapter.in.dto.ProductInSupplierDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface SupplierMapper {
    @Select("SELECT COUNT(*) FROM supplier")
    long countAllSuppliers();
    List<SupplierResponseDto> findSupplierWithPagination(@Param("pageable") Pageable pageable);
    List<ProductInSupplierDto> findProductsBySupplierIds(@Param("supplierIds") List<Long> supplierIds);
}
