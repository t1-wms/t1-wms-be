package com.example.wms.dashboard.adapter.in;

import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import com.example.wms.dashboard.application.port.in.DashboardUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Dashboard 관련 API")
public class DashboardController {
    private final DashboardUseCase dashboardUseCase;

    @GetMapping("/outbound-status")
    public ResponseEntity<OutboundStatusResponseDto> getOutboundStatus() {
        return ResponseEntity.ok(dashboardUseCase.getOutboundStatus());
    }
}
