package com.example.wms.infrastructure.mapper;

import com.example.wms.product.adapter.out.dto.FlatBinDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BinMapper {
    List<FlatBinDto> selectAllBinsWithDetails();
    Long findAvailableBinIdInAisle(String zone, Integer aisle);
    Long findAvailableBinInRow(String zone, Integer aisle, Integer rowNum);
    Long findBinIdByBinCode(String binCode);
    void incrementBinAmount(Long binId, Integer lotCount);
}
