package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LotMapper {
    void updateStatus(Long lotId);
}
