package com.example.wms.infrastructure.mapper;

import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.outbound.application.domain.Outbound;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderProductMapper {
    @Insert("""
        <script>
            INSERT INTO order_product
            (order_id, product_count, product_id, is_defective)
            VALUES
            <foreach collection="products" item="product" separator=",">
                (#{product.orderId}, #{product.productCount}, #{product.productId}, #{product.isDefective})
            </foreach>
        </script>
    """)
    void batchInsert(@Param("products") List<OrderProduct> orderProducts);

    OrderProduct findByProductId(@Param("productId") Long productId);
}
