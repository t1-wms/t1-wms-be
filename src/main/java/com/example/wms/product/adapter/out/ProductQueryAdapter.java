package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.ProductMapper;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductQueryPort;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductQueryAdapter implements ProductQueryPort {

    private final ProductMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        try {
            return productMapper.getAllProducts();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void updateABCGrades() {
        try {
            productMapper.updateABCGrade();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
