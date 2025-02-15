package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.LotMapper;
import com.example.wms.product.application.port.out.LotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LotAdapter implements LotPort {

    private final LotMapper lotMapper;

    @Override
    public void updateStatus(Long lotId) {
        lotMapper.updateStatus(lotId);
    }
}
