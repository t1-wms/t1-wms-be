package com.example.wms.inbound.adapter.in.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundDetailDto {
    private String scheduleNumber;
    private String checkNumber;
    private String putAwayNumber;
    private String scheduleDate;
    private String checkDate;
    private String putAwayDate;
}
