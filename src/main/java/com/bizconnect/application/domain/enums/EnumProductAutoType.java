package com.bizconnect.application.domain.enums;

import lombok.Getter;

@Getter
public enum EnumProductAutoType {
    LITE_1M("autopay_lite_1m_200", "정기결제 라이트 1개월 200건", 10000, 200, 1),
    BASIC_1M("autopay_basic_1m_1000", "정기결제 베이직 1개월 1,000건", 49000, 1000, 1),
    STANDARD_1M("autopay_standard_1m_2000", "정기결제 스탠다드 1개월 2,000건", 96000, 2000, 1),
    PREMIUM_1M("autopay_premium_1m_3000", "정기결제 프리미엄 1개월 3,000건", 144000, 3000, 1),
    ;

    private final String type;
    private final String name;
    private final int price;
    private final int basicOffer;
    private final int month;

    EnumProductAutoType(String type, String name, int price, int basicOffer, int month) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.basicOffer = basicOffer;
        this.month = month;
    }

    public static EnumProductAutoType getProductTypeByString(String type) {
        for (EnumProductAutoType productType : EnumProductAutoType.values()) {
            if (productType.getType().equals(type)) {
                return productType;
            }
        }
        throw new IllegalArgumentException("Invalid productType: " + type);
    }
}
