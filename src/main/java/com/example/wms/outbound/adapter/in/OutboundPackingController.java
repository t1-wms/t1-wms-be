package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.outbound.application.port.in.CreateOutboundPackingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/outboundPacking")
@Tag(name = "출고 패킹 관련 API")
public class OutboundPackingController {
    private CreateOutboundPackingUseCase createOutboundPackingUseCase;

    @PutMapping("register/{outboundPlanId}")
    @Operation(summary = "출고 패킹 등록")
    public ResponseEntity<Void> createOutboundPacking(@PathVariable("outboundPlanId") Long outboundPlanId) {
        Notification notification = createOutboundPackingUseCase.createOutboundPacking(outboundPlanId);
    }

}
