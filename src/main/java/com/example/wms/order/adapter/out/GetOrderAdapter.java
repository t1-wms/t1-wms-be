package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderMapper;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.out.GetOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOrderAdapter implements GetOrderPort {

    private final OrderMapper orderMapper;

    @Override
    public Order getOrder(Long orderId) {
        return orderMapper.findOrderById(orderId);
    }
}
