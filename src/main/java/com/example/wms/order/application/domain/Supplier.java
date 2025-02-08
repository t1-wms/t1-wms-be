package com.example.wms.order.application.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Supplier {
    private Long supplierId;
    private String supplierName;
    private String businessNumber;
    private String representativeName;
    private String address;
    private String supplierPhone;
    private String managerPhone;
}
