package com.bizconnect.application.exceptions.enums;

public enum EnumResultCode {
    SUCCESS("2000", "성공"),
    IllegalArgument("2900", "데이터형식이 올바르지 않습니다."),
    DuplicateMember("3000", "이미 사용중인 아이디입니다."),
    NullPointer("2999", "값이 비어있습니다."),
    NullPointArgument("2999", "제휴사ID 또는 상점ID가 비어있습니다.");



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
