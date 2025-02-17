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
@Schema(description = "작업자 입하 검사 생성 요청 DTO")
public class InboundCheckWorkerReqDto {

    @Schema(description = "품목 id", example = "100")
    private Long productId;

    @Schema(description = "불합격여부", example = "false")
    private Boolean isDefective;

    @Schema(description = "입하예정번호", example = "IS202502060023")
    private String scheduleNumber;
}
