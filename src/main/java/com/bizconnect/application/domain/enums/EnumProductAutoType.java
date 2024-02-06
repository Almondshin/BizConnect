package com.bizconnect.application.domain.enums;

import lombok.Getter;

@Getter
public enum EnumProductAutoType {;
    private final String type;
    private final String name;
    private final int price;
    private final int basicOffer;
    private final int month;
    private final int feePerCase;
    private final int excessFeePerCase;

    EnumProductAutoType(String type, String name, int price, int basicOffer, int month, int feePerCase, int excessFeePerCase) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.basicOffer = basicOffer;
        this.month = month;
        this.feePerCase = feePerCase;
        this.excessFeePerCase = excessFeePerCase;
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
