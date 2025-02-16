package com.example.wms.product.application.port.out;

import com.example.wms.product.adapter.out.dto.FlatBinDto;

import java.util.List;

public interface BinPort {
    List<FlatBinDto> selectAllBinsWithDetails();
    Long findAvailableBinIdInAisle(String zone, Integer aisle);
    Long findBinIdByBinCode(String locationBinCode);
    Long findAvailableBinIdInRow(String zone, Integer aisle, Integer row);
    void incrementBinAmount(Long binId, Integer lotCount);

}
