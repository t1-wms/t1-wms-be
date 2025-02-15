package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.application.domain.Inbound;

import java.time.LocalDate;

public interface InboundPort {
    void save(Inbound inbound);
    void delete(Long inboundId);
    Inbound findById(Long inboundId);
    void updateIC(Long inboundId, LocalDate checkDate, String checkNumber);
}
