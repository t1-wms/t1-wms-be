package com.example.wms.product.application.service;

import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductABCGradeServiceTest {

    @Mock
    private ProductPort productPort;

    @InjectMocks
    private ProductService productService;

    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        mockProducts = Arrays.asList(
                new Product(1L,"a123","handle",5000,10000,10,1L,50, "Electronics","5",7,null,null),
                new Product(1L,"a124","handle2",6000,20000,20,1L,50, "Electronics","5",7,null,null),
                new Product(1L,"a125","handle3",5000,10000,10,1L,50, "Electronics","5",7,null,null)
        );

        when(productPort.getAllProducts()).thenReturn(mockProducts);

        assertFalse(mockProducts.isEmpty());
    }

    @Test
    @DisplayName("품목 별 ABC 등급 부여 테스트")
    void performABCAnalysis() {
        productService.performABCAnalysis();

        verify(productPort, times(mockProducts.size())).updateABCGrades(anyLong(), anyString());
    }

    @Test
    @DisplayName("품목에 bin 코드 부여 테스트")
    void testAssignLocationBinCode() {
        // BIN 배정 실행
        productService.assignLocationBinCode();

        // productPort.updateBinCode()가 제품 수만큼 호출되었는지 검증
        verify(productPort, times(mockProducts.size())).updateBinCode(anyLong(), anyString());
    }
}