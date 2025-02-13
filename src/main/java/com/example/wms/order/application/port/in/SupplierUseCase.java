package com.example.wms.order.application.port.in;

import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierUseCase {

    Page<SupplierResponseDto> getAllSuppliers(Pageable pageable);
}
