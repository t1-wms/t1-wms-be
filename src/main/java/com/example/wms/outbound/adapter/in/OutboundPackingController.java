package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundPackingUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundPackingUseCase;
import com.example.wms.outbound.application.port.in.UpdateOutboundPackingUseCase;
import com.example.wms.user.application.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/outboundPacking")
@Tag(name = "출고 패킹 관련 API")
public class OutboundPackingController {
    private final CreateOutboundPackingUseCase createOutboundPackingUseCase;
    private final NotificationUseCase notificationUseCase;
    private final DeleteOutboundPackingUseCase deleteOutboundPackingUseCase;
    private final UpdateOutboundPackingUseCase updateOutboundPackingUseCase;


    @PutMapping("register/{outboundPlanId}")
    @Operation(summary = "출고 패킹 등록")
    public ResponseEntity<Void> createOutboundPacking(@PathVariable("outboundPlanId") Long outboundPlanId) {
        Notification notification = createOutboundPackingUseCase.createOutboundPacking(outboundPlanId);
        notificationUseCase.send(UserRole.ROLE_ADMIN,notification);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{outboundId}")
    @Operation(summary = "출고 패킹 수정 & 삭제")
    public ResponseEntity<Void> deleteOutboundPacking(
            @PathVariable Long outboundId,
            @RequestBody(required = false) Optional<LocalDate> outboundPackingDate) {
        if (outboundPackingDate.isPresent()) {
            updateOutboundPackingUseCase.updateOutboundPacking(outboundId, outboundPackingDate.get());
        } else {
            deleteOutboundPackingUseCase.deleteOutboundPacking(outboundId);
        }
        return ResponseEntity.ok().build();
    }



}
