package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundAssignUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundAssignUseCase;
import com.example.wms.outbound.application.port.in.UpdateOutboundAssignUseCase;
import com.example.wms.user.application.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/outboundAssign")
@RequiredArgsConstructor
@Tag(name = "출고 지시 관련 API")
public class OutboundAssignController {

    private final CreateOutboundAssignUseCase createOutboundAssignUseCase;
    private final NotificationUseCase notificationUseCase;
    private final DeleteOutboundAssignUseCase deleteOutboundAssignUseCase;
    private final UpdateOutboundAssignUseCase updateOutboundAssignUseCase;

    @PostMapping("/{outboundPlanId}")
    @Operation(summary = "출고 지시 등록", description = "outbound 테이블 생성됨")
    public ResponseEntity<Void> createOutboundAssign(@PathVariable Long outboundPlanId){
        Notification notification = createOutboundAssignUseCase.createOutboundAssign(outboundPlanId);
        notificationUseCase.send(UserRole.ROLE_ADMIN, notification);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{outboundId}")
    @Operation(summary = "출고 지시 수정 & 삭제", description = "RequestBody값 있으면 수정 (outboundAssignDate), 없으면 삭제 (outboundAssignNumber & Date null값 처리)")
    public ResponseEntity<Void> deleteOutboundAssign(
            @PathVariable Long outboundId,
            @RequestBody(required = false) Optional<LocalDate> outboundAssignDate) {

        if (outboundAssignDate.isPresent()) {
            updateOutboundAssignUseCase.updateOutboundAssign(outboundId, outboundAssignDate.get());
        } else {
            deleteOutboundAssignUseCase.deleteOutboundAssign(outboundId);
        }
        return ResponseEntity.ok().build();
    }



}
