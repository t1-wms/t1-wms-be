package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundPutAwayReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundPutAway")
public class InboundPutAwayController {

    private final InboundUseCase inboundUseCase;

    @PostMapping("/{inboundId}")
    @Operation(summary = "입고 적치 생성하기", description = "입고 적치를 생성합니다.")
    public ResponseEntity<Void> createInboundPutAway(@PathVariable Long inboundId, @RequestBody List<InboundPutAwayReqDto> inboundPutAwayReqDtos) {
         inboundUseCase.putAway(inboundId, inboundPutAwayReqDtos);
         return ResponseEntity.status(201).build();
    }
}
