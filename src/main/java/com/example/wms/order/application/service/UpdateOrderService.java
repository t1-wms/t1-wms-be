package com.example.wms.order.application.service;

import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.port.in.UpdateOrderUseCase;
import com.example.wms.order.application.port.out.UpdateOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateOrderService implements UpdateOrderUseCase {

    private final UpdateOrderPort updateOrderPort;

    @Override
    public void updateOrder(Long orderId, List<ProductListDto> productList) {
        updateOrderPort.updateOrder(orderId, productList);
    }
}
