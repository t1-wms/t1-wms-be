package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.ProductMapper;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductPort {

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
    public void updateABCGrades(Long productId, String abcGrade) {
        try {
            productMapper.updateABCGrade(productId, abcGrade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBinCode(Long productId, String binCode) {
        try {
            productMapper.updateBinLocation(productId, binCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> findProductWithPagination(Pageable pageable) {
        return productMapper.findProductWithPagination(pageable);
    }

    @Override
    public long countAllProducts() {
        return productMapper.countAllProducts();
    }
}
