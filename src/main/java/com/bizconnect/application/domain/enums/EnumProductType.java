package com.bizconnect.application.domain.enums;

import lombok.Getter;

@Getter
public enum EnumProductType {
    LITE_12M("lite_12m_2400","라이트 12개월 2,400건", 120000,2400),
    BASIC_12M("basic_12m_12000" , "베이직 12개월 12,000건", 588000,12000),
    STANDARD_12M("standard_12m_24000", "스탠다드 12개월 24,000건", 1152000,24000),
    PREMIUM_12M("premium_12m_36000", "프리미엄 12개월 36,000건", 1728000,36000),
    LITE_6M("lite_6m_1200", "라이트 6개월 1,200건", 60000,1200),
    BASIC_6M("basic_6m_6000", "베이직 6개월 6,000건", 294000,6000),
    STANDARD_6M("standard_6m_12000", "스탠다드 6개월 12,000건", 576000,12000),
    PREMIUM_6M("premium_6m_18000", "프리미엄 6개월 18,000건", 864000,18000),
    LITE_1M("lite_1m_200", "라이트 1개월 200건", 10000,200),
    BASIC_1M("basic_1m_1000", "베이직 1개월 1,000건", 49000,1000),
    STANDARD_1M("standard_1m_2000", "스탠다드 1개월 2,000건", 96000,2000),
    PREMIUM_1M("premium_1m_3000", "프리미엄 1개월 3,000건", 144000,3000),
    ;

    private final String type;
    private final String name;
    private final int price;
    private final int basicOffer;

    EnumProductType(String type, String name, int price, int basicOffer) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.basicOffer = basicOffer;
    }
}
