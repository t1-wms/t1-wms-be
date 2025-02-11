package com.example.wms.product.adapter.in;

import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping("/product")
    public ResponseEntity<Page<Product>> findAllBinsWithDetails(Pageable pageable) {
        return ResponseEntity.ok(productUseCase.getAllProducts(pageable));
    }
}
