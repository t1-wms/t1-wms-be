package com.example.wms.product.adapter.in;

import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
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
    public ResponseEntity<Page<Product>> findAllBinsWithDetails(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(productUseCase.getAllProducts(pageable));
    }
}
