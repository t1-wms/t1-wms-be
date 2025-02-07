package com.example.wms.user.application.domain.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("일반"),
    ROLE_SUPPLIER("공급업체"),
    ROLE_WORKER("작업자"),
    ROLE_ADMIN("관리자");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public static UserRole getUserRole(String value) {
        if (value.contains("관리자")) {
            return ROLE_ADMIN;
        } else if (value.contains("공급업체")) {
            return ROLE_SUPPLIER;
        } else if (value.contains("작업자")) {
            return ROLE_WORKER;
        } else {
            return ROLE_USER;
        }
    }
}
