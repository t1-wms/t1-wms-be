package com.example.wms.infrastructure.enums;

public enum ExceptionMessage {
    AUTHENTICATION_FAILED("인증 실패했습니다."),
    AUTHORIZATION_FAILED("접근 권한이 없습니다."),
    DUPLICATED("이미 존재하는 데이터입니다."),
    UNAUTHORIZED("인증이 필요합니다."),
    NOT_FOUND("요청한 데이터를 찾을 수 없습니다."),
    ILLEGAL_ARGUMENT("잘못된 요청입니다."),
    FORBIDDEN("권한이 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
