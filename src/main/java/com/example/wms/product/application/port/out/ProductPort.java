package com.example.wms.product.application.port.out;

import com.example.wms.product.application.domain.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductPort {

    List<Product> getAllProducts();
    void updateABCGrades();
    void updateBinCode();
    List<Product> findProductWithPagination(Pageable pageable);
    long countAllProducts();
}
