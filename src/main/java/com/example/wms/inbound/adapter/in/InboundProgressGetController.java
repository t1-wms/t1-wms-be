package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundProgressResDto;
import com.example.wms.inbound.application.service.InboundService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundProcess")
public class InboundProgressGetController {

    private final InboundService inboundService;

    @GetMapping
    @Operation(summary = "입고진행별 입고 목록 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<InboundProgressResDto>> getAllInboundProgress(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable) {
        Page<InboundProgressResDto> response = inboundService.getAllInboundProgressWithPagination(startDate, endDate, pageable);
        return ResponseEntity.ok(response);
    }
}
