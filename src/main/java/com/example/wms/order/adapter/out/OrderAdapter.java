package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.infrastructure.mapper.OrderMapper;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OrderAdapter implements OrderPort {

    private final ProductPort productPort;
    private final OrderMapper orderMapper;

    @Override
    public void createOrder(Long productId, Long defectiveLotCount) {
        Product product = productPort.findById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found for ID : " + productId);
        }

        String orderNumber = generateOrderNumber();

        Order order = Order.builder()
                .supplierId(product.getSupplierId())
                .orderNumber(orderNumber)
                .orderDate(LocalDate.now())
                //.orderQuantity(Math.toIntExact(defectiveLotCount))
                .isApproved(true)
                .isReturnOrder(true)
                .orderStatus("처리중")
                .inboundDate(LocalDate.now().plusDays(product.getLeadTime()))
                .build();

        orderMapper.createOrder(order);
    }


    private String generateOrderNumber() {
        String prefix = "OR" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String lastOrderNumber = orderMapper.getLastOrderNumber();

        int newNumber = 0;

        if (lastOrderNumber != null) {
            String lastNumberStr = lastOrderNumber.substring(10);
            newNumber = Integer.parseInt(lastNumberStr) + 1;
        }

        return prefix + String.format("%04d", newNumber);
    }
}
