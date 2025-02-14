package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.ProductMapper;
import com.example.wms.product.adapter.in.dto.ProductOverviewDto;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductPort {

    private final ProductMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        return executeWithFallback(productMapper::getAllProducts, Collections.emptyList());
    }

    @Override
    public void updateABCGrades(Long productId, String abcGrade) {
        executeWithFallback(()-> productMapper.updateABCGrade(productId, abcGrade));
    }

    @Override
    public void updateBinCode(Long productId, String binCode) {
        executeWithFallback(()-> productMapper.updateBinLocation(productId, binCode));
    }

    @Override
    public List<Product> findProductWithPagination(Pageable pageable) {
        return productMapper.findProductWithPagination(pageable);
    }

    @Override
    public long countAllProducts() {
        return productMapper.countAllProducts();
    }

    @Override
    public List<ProductOverviewDto> findProductOverview() {
        return productMapper.selectProductOverview();
    }

    private <T> T executeWithFallback(Supplier<T> action, T fallback) {
        try {
            return action.get();
        } catch (Exception e) {
            return fallback;
        }
    }

    private void executeWithFallback(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
