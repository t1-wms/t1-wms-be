package com.example.wms.order.application.service;

import com.example.wms.inbound.application.service.InboundService;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.in.GetApprovedOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetApprovedOrderService implements GetApprovedOrderUseCase {

    private final InboundService inboundService;


    // 발주 등록 시 호출
    // 호출 전 발주 등록 부분에서 orderStatus를 "APPROVED" 로 변경해야 함
    @Override
    @Transactional
    public void processOrderApproval(Order order) {
        if (Boolean.TRUE.equals(order.getIsApproved())) {
            inboundService.createInboundSchedule(order);
        }
    }
}
