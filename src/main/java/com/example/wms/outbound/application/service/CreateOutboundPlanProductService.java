package com.example.wms.outbound.application.service;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanProductUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanProductPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOutboundPlanProductService implements CreateOutboundPlanProductUseCase {
    private final CreateOutboundPlanProductPort createOutboundPlanProductPort;
    private final NotificationPort notificationPort;

    @Override
    public Notification createOutboundPlanProduct(Long outboundPlanId, List<ProductInfoDto> productInfoDtoList) {

        List<OutboundPlanProduct> outboundPlanProductList = productInfoDtoList.stream()
                .map(dto -> OutboundPlanProduct.builder()
                        .outboundPlanId(outboundPlanId)
                        .productId(dto.getProductId())
                        .requiredQuantity(dto.getProductCount()) // 사용자 주문서에 적혀있는 수량
                        .stockUsedQuantity(50) // 재고에서 사용한 수량
                        .orderQuantity(100) // 발주 없으면 0, 있으면 주문수량
                        .status("진행중")
                        .build())
                .collect(Collectors.toList());

        createOutboundPlanProductPort.saveAll(outboundPlanProductList);
        // 알림 저장하기 & 보내기
        Notification notification = Notification.builder()
                .content("출고 예정이 생성되었습니다.")
                .event("출고 예정")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}
