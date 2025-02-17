package com.example.wms.inbound.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "품목 별 입하 검사 결과 등록 요청 dto")
public class InboundCheckedProductReqDto {

    @Schema(description = "품목 id", example = "101")
    private Long productId;

    @Schema(description = "품목별 불합격 로트 수량", example = "50")
    private Long defectiveLotCount;
}
