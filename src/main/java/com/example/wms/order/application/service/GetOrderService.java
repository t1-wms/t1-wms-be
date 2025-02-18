package com.example.wms.order.application.service;

import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.in.GetOrderUseCase;
import com.example.wms.order.application.port.out.GetOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOrderService implements GetOrderUseCase {

    private final GetOrderPort getOrderPort;

    @Override
    public Order getOrder(Long orderId) {
        return getOrderPort.getOrder(orderId);
    }
}
