package com.example.wms.inbound.application.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundCheck {
    private Long inboundCheckId;
    private Long inboundId;
    private Long productId;
    private Long defectiveLotCount;
}
