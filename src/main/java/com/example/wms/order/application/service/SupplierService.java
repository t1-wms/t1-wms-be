package com.example.wms.order.application.service;

import com.example.wms.order.application.port.in.SupplierUseCase;
import com.example.wms.order.application.port.out.SupplierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService implements SupplierUseCase {

    private final SupplierPort supplierPort;

}
