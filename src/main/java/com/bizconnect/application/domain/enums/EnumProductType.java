package com.bizconnect.application.domain.enums;

import com.bizconnect.application.exceptions.enums.EnumResultCode;
import lombok.Getter;

@Getter
public enum EnumProductType {
    //    LITE_12M("lite_12m_2400", "라이트 12개월 2,400건", 120000, 2400, 12),
//    BASIC_12M("basic_12m_12000", "베이직 12개월 12,000건", 588000, 12000, 12),
//    STANDARD_12M("standard_12m_24000", "스탠다드 12개월 24,000건", 1152000, 24000, 12),
//    PREMIUM_12M("premium_12m_36000", "프리미엄 12개월 36,000건", 1728000, 36000, 12),
//    LITE_6M("lite_6m_1200", "라이트 6개월 1,200건", 60000, 1200, 6),
//    BASIC_6M("basic_6m_6000", "베이직 6개월 6,000건", 294000, 6000, 6),
//    STANDARD_6M("standard_6m_12000", "스탠다드 6개월 12,000건", 576000, 12000, 6),
//    PREMIUM_6M("premium_6m_18000", "프리미엄 6개월 18,000건", 864000, 18000, 6),
    LITE_1M("lite_1m_200", "라이트 1개월 200건", 10000, 200, 1, 50, 50),
    BASIC_1M("basic_1m_1000", "베이직 1개월 1,000건", 49000, 1000, 1, 49, 50),
    STANDARD_1M("standard_1m_2000", "스탠다드 1개월 2,000건", 96000, 2000, 1, 48, 50),
    PREMIUM_1M("premium_1m_3000", "프리미엄 1개월 3,000건", 144000, 3000, 1, 48, 50),
    AUTO_LITE_1M("autopay_lite_1m_200", "정기결제 라이트 1개월 200건", 10000, 200, 1,50,50),
    AUTO_BASIC_1M("autopay_basic_1m_1000", "정기결제 베이직 1개월 1,000건", 49000, 1000, 1,49,50),
    AUTO_STANDARD_1M("autopay_standard_1m_2000", "정기결제 스탠다드 1개월 2,000건", 96000, 2000, 1,48,50),
    AUTO_PREMIUM_1M("autopay_premium_1m_3000", "정기결제 프리미엄 1개월 3,000건", 144000, 3000, 1,48,50),
    ;

    private final String type;
    private final String name;
    private final int price;
    private final int basicOffer;
    private final int month;
    private final int feePerCase;
    private final int excessFeePerCase;

    EnumProductType(String type, String name, int price, int basicOffer, int month, int feePerCase, int excessFeePerCase) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.basicOffer = basicOffer;
        this.month = month;
        this.feePerCase = feePerCase;
        this.excessFeePerCase = excessFeePerCase;
    }

    public static EnumProductType getProductTypeByString(String type) {
        for (EnumProductType productType : EnumProductType.values()) {
            if (productType.getType().equals(type)) {
                return productType;
            }
        }
        throw new IllegalArgumentException("Invalid productType: " + type);
    }
}
