package com.example.wms.outbound.adapter.in;

import com.example.wms.outbound.adapter.in.dto.OutboundRequestDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.port.in.CreateOutboundUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/outbound")
@Tag(name = "출고 예정 생성 API", description = "출고 예정 생성 관련 API입니다.")
public class OutboundController {

    private final CreateOutboundUseCase createOutboundUseCase;

    @PostMapping
    @Operation(summary = "출고 예정 생성하기", description = "출고 예정 생성하기 입니다.")
    public ResponseEntity<Void> createOutbound(@RequestBody OutboundRequestDto outboundRequestDto) {
        createOutboundUseCase.createOutbound(outboundRequestDto);
        return ResponseEntity.status(201).build();
    }

}
