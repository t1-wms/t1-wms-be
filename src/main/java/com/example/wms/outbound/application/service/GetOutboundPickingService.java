package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import com.example.wms.outbound.application.port.in.GetOutboundPickingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GetOutboundPickingService implements GetOutboundPickingUseCase {
    @Override
    public Page<OutboundPickingResponseDto> getFilteredOutboundPickings(String outboundPickingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return null;
    }
}
