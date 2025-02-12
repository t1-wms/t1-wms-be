package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.BinMapper;
import com.example.wms.product.adapter.out.dto.FlatBinDto;
import com.example.wms.product.application.port.out.BinPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BinAdapter implements BinPort {
    private final BinMapper binMapper;

    @Override
    public List<FlatBinDto> selectAllBinsWithDetails() {
        return binMapper.selectAllBinsWithDetails();
    }
}
