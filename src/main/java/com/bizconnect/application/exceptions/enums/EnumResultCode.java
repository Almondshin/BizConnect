package com.bizconnect.application.exceptions.enums;

public enum EnumResultCode {
    SUCCESS("2000", "성공"),
    IllegalArgument("2900", "데이터형식이 올바르지 않습니다."),
    DuplicateMember("3000", "이미 사용중인 아이디입니다."),
    UnregisteredAgency("2999", "등록되지 않은 가맹점입니다."),
    PendingApprovalStatus("2800", "제휴사 승인 대기 상태입니다."),
    SuspendedSiteId("3999", "이용정지된 사이트 아이디입니다."),
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
