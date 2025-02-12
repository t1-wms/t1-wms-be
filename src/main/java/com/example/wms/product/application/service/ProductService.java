package com.example.wms.product.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    @Override
    public void performABCAnalysis() {

        productPort.updateABCGrades();
        List<Product> abcProducts = productPort.getAllProducts();

        if (!abcProducts.isEmpty()) {
            abcProducts.forEach(product ->
                    System.out.println("Product: " + product.getProductCode() + ", ABC Grade: " + product.getAbcGrade())
            );
        }
    }

    @Override
    public void assignLocationBinCode() {
        productPort.updateBinCode();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        // 외부에서 받은 Pageable을 안전하게 변환 (유효하지 않은 정렬 조건이 있으면 예외 발생)
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Product.class);

        // safePageable을 Mapper에 전달해서 DB 쿼리를 실행
        // (여기서는 Mapper의 반환타입이나 사용법에 따라 적절히 변환)
        List<Product> productList = productPort.findProductWithPagination(safePageable);
        long count = productPort.countAllProducts();

        // PageImpl을 사용하여 Page 객체로 감싸서 반환하는 예시
        return new PageImpl<>(productList, safePageable, count);
    }
}
