package com.example.wms.product.application.service;

import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import com.example.wms.product.application.port.out.ProductQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductQueryPort productQueryPort;

    @Override
    public void performABCAnalysis() {

        productQueryPort.updateABCGrades();
        List<Product> abcProducts = productQueryPort.getAllProducts();

        if (!abcProducts.isEmpty()) {
            abcProducts.forEach(product ->
                    System.out.println("Product: " + product.getProductCode() + ", ABC Grade: " + product.getAbcGrade())
            );
        }
    }
}
