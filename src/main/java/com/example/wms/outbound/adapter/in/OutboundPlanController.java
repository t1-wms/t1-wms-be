package com.example.wms.outbound.adapter.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanProductUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundPlanProductUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundPlanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequiredArgsConstructor
@RequestMapping("/outbound")
@Tag(name = "출고 예정 관련 API")
public class OutboundPlanController {

    private final CreateOutboundPlanUseCase createOutboundPlanUseCase;
    private final CreateOutboundPlanProductUseCase createOutboundPlanProductUseCase;
    private final DeleteOutboundPlanUseCase deleteOutboundPlanUseCase;
    private final DeleteOutboundPlanProductUseCase deleteOutboundPlanProductUseCase;

    @PostMapping
    @Operation(summary = "출고 예정 생성하기", description = "outboundPlan & outboundPlanProduct 생성됨 201반환")
    public ResponseEntity<Void> createOutbound(@RequestBody OutboundPlanRequestDto outboundPlanRequestDto) {
        Long outboundPlanId = createOutboundPlanUseCase.createOutbound(outboundPlanRequestDto);
        createOutboundPlanProductUseCase.createOutboundPlanProduct(outboundPlanId,outboundPlanRequestDto.getProductList());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{outboundPlanId}")
    @Operation(summary = "출고 예정 삭제하기", description = "outboundPlan & outboundPlanProduct 삭제됨 204반환")
    public ResponseEntity<Void> deleteOutbound(@PathVariable Long outboundPlanId) {
//        deleteOutboundPlanProductUseCase.deleteOutboundPlanProduct(outboundPlanId);
//        deleteOutboundPlanUseCase.deleteOutboundPlan(outboundPlanId);
        deleteOutboundPlanUseCase.deleteOutboundPlanAndProducts(outboundPlanId);
        return ResponseEntity.noContent().build();
    }

}
