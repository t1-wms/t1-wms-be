package com.example.wms.inbound.adapter.in.dto.response;

import com.example.wms.product.adapter.in.dto.LotInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class InboundWorkerCheckResDto {
    private String checkNumber;
    private List<LotInfoDto> lots;
}
