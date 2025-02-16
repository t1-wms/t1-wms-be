package com.example.wms.product.application.port.out;

import com.example.wms.product.application.domain.Lot;

public interface LotPort {
    void updateStatus(Long lotId);
    void insertLot(Lot lot);
}
