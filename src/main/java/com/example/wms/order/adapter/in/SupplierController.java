package com.example.wms.order.adapter.in;

import com.example.wms.order.application.port.in.SupplierUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierUseCase supplierUseCase;


}
