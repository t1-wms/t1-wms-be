package com.example.wms.product.adapter.in;

import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product", description = "Product 관련 API")
public class ProductController {

    private final ProductUseCase productUseCase;

    @PostMapping("/abc-analysis")
    public ResponseEntity<Void> performABCAnalysis() {
        productUseCase.performABCAnalysis();
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/assign-bin")
    public ResponseEntity<String> assignLocationBinCode() {
        productUseCase.assignLocationBinCode();
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(
            summary = "전체 품목 조회",
            description = "전체 품목 목록을 페이지별로 조회할 수 있습니다."
    )
    public ResponseEntity<Page<Product>> findAllBinsWithDetails(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(productUseCase.getAllProducts(pageable));
    }
}
