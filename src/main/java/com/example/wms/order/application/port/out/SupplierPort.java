package com.example.wms.order.application.port.out;

import com.example.wms.order.adapter.in.dto.ProductInSupplierDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierPort {
    long countAllSuppliers();
    List<SupplierResponseDto> findSupplierWithPagination(Pageable pageable);
    List<ProductInSupplierDto> findProductsBySupplierIds(List<Long> supplierIds);
}
