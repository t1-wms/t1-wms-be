package com.example.wms.order.application.port.in;

import com.example.wms.order.application.domain.Order;

public interface GetOrderUseCase {
    Order getOrder(Long orderId);
}
