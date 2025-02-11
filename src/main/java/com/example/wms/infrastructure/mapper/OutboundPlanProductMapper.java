package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OutboundPlanProductMapper {
    @Insert("""
        <script>
            INSERT INTO outbound_plan_product
            (outbound_plan_id, product_id, required_quantity, stock_used_quantity, order_quantity, status)
            VALUES
            <foreach collection="products" item="product" separator=",">
                (#{product.outboundPlanId}, #{product.productId}, #{product.requiredQuantity}, #{product.stockUsedQuantity}, #{product.orderQuantity}, #{product.status})
            </foreach>
        </script>
    """)
    void batchInsert(@Param("products") List<OutboundPlanProduct> outboundPlanProductList);
}
