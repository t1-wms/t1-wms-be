package com.example.wms.product.application.port.out;

import com.example.wms.product.adapter.out.dto.FlatBinDto;

import java.util.List;

public interface BinPort {
    List<FlatBinDto> selectAllBinsWithDetails();
}
