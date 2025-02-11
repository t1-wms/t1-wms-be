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

        beforeUpdate.forEach(p ->
                System.out.println("Before Update - Product: " + p.getProductId() + ", ABC Grade: " + p.getAbcGrade())
        );

        productService.performABCAnalysis();

        List<Product> afterUpdate = productQueryPort.getAllProducts();
        assertNotNull(afterUpdate, "변경된 데이터가 있어야 합니다. ");
        afterUpdate.forEach(p ->
                System.out.println("ABC Grade:" + p.getAbcGrade()));
    }

    @Test
    void updateBinCode_ShouldActuallyUpdateDatabases() {
        List<Product> beforeUpdate = productQueryPort.getAllProducts();
        for(int i=0; i<10; i++) {
            System.out.println(beforeUpdate.get(i).getLocationBinCode()+" " + beforeUpdate.get(i).getAbcGrade());
        }

        productQueryPort.updateBinCode();

        List<Product> afterUpdate = productQueryPort.getAllProducts();
        for(int i=0;i<10; i++) {
            System.out.println(afterUpdate.get(i).getLocationBinCode()+" " + afterUpdate.get(i).getAbcGrade());
        }
    }

}