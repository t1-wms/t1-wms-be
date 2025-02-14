package com.example.wms.order.adapter.in;

import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import com.example.wms.order.application.port.in.SupplierUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier")
@Tag(name = "Supplier", description = "Supplier 관련 API")
public class SupplierController {

    private final SupplierUseCase supplierUseCase;

    @GetMapping
    @Operation(
            summary = "전체 공급업체 조회",
            description = "전체 공급업체 목록을 페이지별로 조회할 수 있습니다."
    )
    ResponseEntity<Page<SupplierResponseDto>> getAllSuppliers(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierUseCase.getAllSuppliers(pageable));
    }

}
