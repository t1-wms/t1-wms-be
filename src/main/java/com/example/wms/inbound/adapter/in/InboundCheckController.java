package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundCheck")
public class InboundCheckController {

    private final InboundUseCase inboundUseCase;

    @PostMapping("/{inboundId}")
    @Operation(summary = "입하 검사 관리자가 등록하기", description = "입하 검사를 등록합니다.")
    public ResponseEntity<Void> createInboundCheck(@RequestBody InboundCheckReqDto inboundCheckReqDto) {
        inboundUseCase.createInboundCheck(inboundCheckReqDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "입하 검사 목록 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<InboundResDto>> getInboundCheck(
            @RequestParam(value = "inboundCheckNumber", required = false) String inboundCheckNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(inboundUseCase.getFilteredInboundCheck(inboundCheckNumber, startDate, endDate, pageable));
    }

    @PutMapping("/{inboundId}")
    @Operation(summary = "입하 검사 수정하기", description= "입하 검사를 수정합니다.")
    public ResponseEntity<?> updateOutboundPlan(@PathVariable Long inboundId, @RequestBody InboundCheckUpdateReqDto inboundCheckUpdateReqDto) {
        inboundUseCase.updateInboundCheck(inboundId, inboundCheckUpdateReqDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{inboundId}")
    @Operation(summary ="입하 검사 삭제하기", description = "입하 검사를 삭제합니다.")
    public ResponseEntity<Void> deleteInboundCheck(@PathVariable Long inboundId) {
        inboundUseCase.deleteInboundCheck(inboundId);
        return ResponseEntity.noContent().build();
    }
}
