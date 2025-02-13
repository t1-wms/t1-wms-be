package com.example.wms.infrastructure.mapper;

import com.example.wms.product.application.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> getAllProducts();
    void updateABCGrade(@Param("productId") Long productId, @Param("abcGrade") String abcGrade);
    void updateBinLocation(@Param("productId") Long productId, @Param("binCode") String locationBinCode);

    List<Product> findProductWithPagination(@Param("pageable") Pageable pageable);
    long countAllProducts();
}
