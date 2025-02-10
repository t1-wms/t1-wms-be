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

@RestController
@RequiredArgsConstructor
@RequestMapping("/outbound")
public class OutboundController {

    private final CreateOutboundUseCase createOutboundUseCase;

    @PostMapping
    public ResponseEntity<Void> createOutbound(@RequestBody OutboundRequestDto outboundRequestDto) {
        createOutboundUseCase.createOutbound(outboundRequestDto);
        return ResponseEntity.status(201).build();
    }

}
