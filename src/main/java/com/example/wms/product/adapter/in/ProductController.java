package com.example.wms.product.adapter.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
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

    public ResponseEntity<Page<Product>> findAllBinsWithDetails(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(productUseCase.getAllProducts(pageable));
    }
}
