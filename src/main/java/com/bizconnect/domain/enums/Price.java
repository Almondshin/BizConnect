package com.bizconnect.domain.enums;

public enum Price {
    CHARGE_6_LIGHT("","충전제 6개월 라이트"),
    CHARGE_6_BASIC("","충전제 6개월 베이직"),
    CHARGE_6_STANDARD("","충전제 6개월 스텐다드"),
    CHARGE_6_PREMIUM("","충전제 6개월 프리미엄"),
    CHARGE_12_LIGHT("","12개월 라이트 요금제"),
    CHARGE_12_BASIC("","12개월 라이트 요금제"),
    CHARGE_12_STANDARD("","12개월 라이트 요금제"),
    CHARGE_12_PREMIUM("","12개월 라이트 요금제"),
    MONTHLY_LIGHT("","월정액 라이트 요금제"),
    MONTHLY_BASIC("","월정액 베이직 요금제"),
    MONTHLY_STANDARD("","월정액 스탠다드 요금제"),
    MONTHLY_PREMIUM("","월정액 프리미엄 요금제");



    private String code;
    private String value;

    Price(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }
    public String getValue() {
        return value;
    }
}

