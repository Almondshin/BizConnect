package com.bizconnect.domain.vo;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ProductPriceInfoVO {
    private String productType;
    private int price;
    private int providedItemCount;
    private String pricePerItem;
    private String excessChargePerItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPriceInfoVO that = (ProductPriceInfoVO) o;
        return price == that.price
                && providedItemCount == that.providedItemCount
                && Objects.equals(productType, that.productType)
                && Objects.equals(pricePerItem, that.pricePerItem)
                && Objects.equals(excessChargePerItem, that.excessChargePerItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productType, price, providedItemCount, pricePerItem, excessChargePerItem);
    }
}
