package com.example.wms.inventory.adapter.in;

import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.application.port.in.InventoryUseCase;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory 관련 API")
public class InventoryController {

    private final InventoryUseCase inventoryUseCase;

    @GetMapping("/productStock")
    @Operation(
            summary = "품목별 전체 재고 조회",
            description = "전체 품목에 대한 품목별 재고량을 페이지별로 조회할 수 있습니다.\n" +
                    "- **품목 코드 필터링:** 요청 파라미터로 `productCode` 값을 전달하면 해당 문자열이 포함된 품목만 조회합니다.\n" +
                    "- **전체 조회:** `productCode`가 null 또는 빈 문자열일 경우 전체 품목을 조회합니다.\n"
    )
    public ResponseEntity<Page<ProductInfoDto>> getAllProductInventories(@RequestParam(required = false) String productCode, @ParameterObject
    Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryUseCase.getAllProductInventories(productCode, pageable));
    }

    @GetMapping("/productThreshold")
    @Operation(
            summary = "품목별 전체 재고 임계값 조회",
            description = "전체 품목에 대한 품목별 재고 임계값을 페이지별로 조회할 수 있습니다.\n" +
                    "- **품목 코드 필터링:** 요청 파라미터로 `productCode` 값을 전달하면 해당 문자열이 포함된 품목만 조회합니다.\n" +
                    "- **전체 조회:** `productCode`가 null 또는 빈 문자열일 경우 전체 품목을 조회합니다.\n"
    )
    public ResponseEntity<Page<ProductThresholdDto>> getAllProductThresholds(@RequestParam(required = false) String productCode, @ParameterObject
    Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryUseCase.getAllProductThresholds(productCode, pageable));
    }
}
