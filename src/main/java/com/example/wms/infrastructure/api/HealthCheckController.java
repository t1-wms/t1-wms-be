package com.example.wms.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("Test API 성공");
    }
}