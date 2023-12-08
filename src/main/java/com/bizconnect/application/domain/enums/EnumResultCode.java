package com.bizconnect.application.domain.enums;


public enum EnumResultCode {

    SUCCESS("2000", "성공"),
    FAIL("2099", "실패");

    private final String code;
    private final String value;

    EnumResultCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static String fromCode(String code) {
        for (EnumResultCode resultCode : EnumResultCode.values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode.getCode();
            }
        }
        throw new IllegalArgumentException("Invalid resultCode: " + code);
    }

}
