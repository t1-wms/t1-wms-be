package com.example.wms.product.application.service;

import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductQueryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductQueryPort productQueryPort;

    @Autowired
    private ProductService productService;

    @Test
    void updateABCGrades_ShouldActuallyUpdateDatabase() {

        List<Product> beforeUpdate = productQueryPort.getAllProducts();
        assertNotNull(beforeUpdate, "기존 데이터가 있어야 합니다.");

        beforeUpdate.forEach(product ->
                System.out.println("Before Update - Product: " + product.getLocationBinCode() + ", ABC Grade: " + product.getAbcGrade())
        );

        productService.performABCAnalysis();

        List<Product> afterUpdate = productQueryPort.getAllProducts();
        assertNotNull(afterUpdate, "변경된 데이터가 있어야 합니다. ");
    }


}