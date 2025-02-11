package com.example.wms.outbound.adapter.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanProductUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequiredArgsConstructor
@RequestMapping("/outbound")
@Tag(name = "출고 예정 생성 API", description = "outboundPlan & ouboundPlanProduct 생성됨")
public class OutboundPlanController {

    private final CreateOutboundPlanUseCase createOutboundPlanUseCase;
    private final CreateOutboundPlanProductUseCase createOutboundPlanProductUseCase;

    @PostMapping
    @Operation(summary = "출고 예정 생성하기", description = "출고 예정 생성하기 테스트입니다.")
    public ResponseEntity<Void> createOutbound(@RequestBody OutboundPlanRequestDto outboundPlanRequestDto) {
        Long outboundPlanId = createOutboundPlanUseCase.createOutbound(outboundPlanRequestDto);
        createOutboundPlanProductUseCase.createOutboundPlanProduct(outboundPlanId,outboundPlanRequestDto.getProductList());
        return ResponseEntity.status(201).build();
    }

}
