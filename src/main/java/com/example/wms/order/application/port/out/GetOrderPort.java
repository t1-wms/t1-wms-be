package com.example.wms.order.application.port.out;

import com.example.wms.order.application.domain.Order;

public interface GetOrderPort {
    Order getOrder(Long orderId);
}
