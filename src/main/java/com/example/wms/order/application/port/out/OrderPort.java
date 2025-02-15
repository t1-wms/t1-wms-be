package com.example.wms.order.application.port.out;

public interface OrderPort {
    void createOrder(Long productId, Long defectiveLotCount);
}
