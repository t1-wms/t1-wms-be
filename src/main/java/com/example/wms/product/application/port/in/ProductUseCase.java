package com.example.wms.product.application.port.in;

import com.example.wms.product.application.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductUseCase {
    void performABCAnalysis();
    void assignLocationBinCode();
    Page<Product> getAllProducts(Pageable pageable);
}
