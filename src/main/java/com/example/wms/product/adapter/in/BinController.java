package com.example.wms.product.adapter.in;

import com.example.wms.product.adapter.in.dto.BinResponseDto;
import com.example.wms.product.application.port.in.BinUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BinController {
    private final BinUseCase binUseCase;

    @GetMapping("/bin")
    public ResponseEntity<List<BinResponseDto>> findAllBinsWithDetails() {
        return ResponseEntity.ok(binUseCase.findAllBinsWithDetails());
    }
}
