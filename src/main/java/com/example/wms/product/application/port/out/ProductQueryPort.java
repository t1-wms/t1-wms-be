package com.example.wms.product.application.port.out;

import com.example.wms.product.application.domain.Product;
import java.util.List;

public interface ProductQueryPort {

    List<Product> getAllProducts();
    void updateABCGrades();
}
