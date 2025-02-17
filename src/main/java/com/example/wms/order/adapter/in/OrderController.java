package com.example.wms.order.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.order.adapter.in.dto.OrderRequestDto;
import com.example.wms.order.application.port.in.RegisterOrderProductUseCase;
import com.example.wms.order.application.port.in.RegisterOrderUseCase;
import com.example.wms.user.application.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "발주 관련 API")
public class OrderController {

    private final RegisterOrderUseCase registerOrderUseCase;
    private final RegisterOrderProductUseCase registerOrderProductUseCase;
    private final NotificationUseCase notificationUseCase;

    @PostMapping
    @Operation(summary = "발주 등록하기")
    public ResponseEntity<Void> order(@RequestBody OrderRequestDto orderRequestDto) {
        Long orderId = registerOrderUseCase.registerOrder(orderRequestDto);
        Notification notification = registerOrderProductUseCase.registerOrderProduct(orderId, orderRequestDto.getProductList());
        notificationUseCase.send(UserRole.ROLE_ADMIN, notification);
        return ResponseEntity.ok().build();
    }

    

}
