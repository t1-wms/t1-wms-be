package com.example.wms.infrastructure.mapper;

import com.example.wms.product.adapter.out.dto.FlatBinDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BinMapper {
    List<FlatBinDto> selectAllBinsWithDetails();
}
