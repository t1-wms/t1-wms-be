package com.example.wms.inbound.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "입하 검사 수정 요청 DTO")
public class InboundCheckUpdateReqDto {

    @Schema(description = "입고 id", example = "100")
    private Long inboundId;

    @Schema(description = "입하 검사일", example = "2025-02-15")
    private String checkDate;

    @Schema(description = "입하 검사 수정 항목들", example = "{\n" +
            "checkDate(검사일): String\n" +
            "productList(품목 리스트): [\n" +
            "  {\n" +
            "  productId: Long\n" +
            "  defectiveLotCount(불합격 수량): Long\n" +
            "  }, …\n" +
            "]\n" +
            "}")
    private List<InboundCheckedProductReqDto> checkedProductList;
}
