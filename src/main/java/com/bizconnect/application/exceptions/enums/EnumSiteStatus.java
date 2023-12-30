package com.bizconnect.application.exceptions.enums;

public enum EnumSiteStatus {
    UNREGISTERED("U", "제휴사 미등록"),
    DUPLICATE("D", "이미 등록된 사이트"),
    ACTIVE("A" , "사이트 사용중"),
    PENDING("P" , "제휴사 승인대기"),
    SUSPENDED("S" , "사이트 이용정지");

    private final String code;
    private final String value;

    EnumSiteStatus(String code, String value) {
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
        for (EnumSiteStatus siteStatus : EnumSiteStatus.values()) {
            if (siteStatus.getCode().equals(code)) {
                return siteStatus.getCode(); // 여기서 EnumResultCode의 code 값을 반환
            }
        }
        throw new IllegalArgumentException("Invalid resultCode: " + code);
    }

}
