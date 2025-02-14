package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPlanProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inbound")
public class InboundController {

    private final InboundUseCase inboundUseCase;

    public ResponseEntity<Void> createInbound(@RequestBody InboundReqDto inboundReqDto) {
        inboundUseCase.createInboundPlan(inboundReqDto); // 수동 생성
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "입고 예정 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<InboundResDto>> getInboundPlans(
            @RequestParam(value = "inboundScheduleNumber", required = false) String inboundScheduleNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable) {
        Page<InboundResDto> result;

        if (inboundScheduleNumber != null || startDate != null || endDate != null) {
            result = inboundUseCase.getFilteredInboundPlans(inboundScheduleNumber, startDate, endDate, pageable);
        } else {
            result = inboundUseCase.getInboundPlans(pageable);
        }
        return ResponseEntity.ok(result);
    }



}
