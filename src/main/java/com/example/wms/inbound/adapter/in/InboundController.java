package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inbound")
public class InboundController {

    private final InboundUseCase inboundUseCase;

    public ResponseEntity<Void> createInbound(@RequestBody InboundReqDto inboundReqDto) {
        inboundUseCase.createInboundPlan(inboundReqDto); // 수동 생성
        return ResponseEntity.status(201).build();
    }
}
