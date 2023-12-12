package com.bizconnect.adapter.in.enums;


public enum EnumResultCode {
    SUCCESS("2000", "성공"),
    FAIL("2099", "실패"),
    IllegalArgument("2097", "데이터형식이 올바르지 않습니다."),
    DuplicateMember("2096", "이미 사용중인 아이디입니다."),
    NO_CONTENT("2098", "값이 비어있음");

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
