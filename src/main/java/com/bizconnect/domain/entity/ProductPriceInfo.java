package com.bizconnect.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "PRODUCT_PRICE_INFO")
public class ProductPriceInfo {
    /*
        상품종류 (Product Type): productType
        요금 (Price): price
        제공건수 (Number of Provided Items): providedItemCount
        건당요금 (Price Per Item): pricePerItem
        건당 초과 요금 (Excess Charge Per Item): excessChargePerItem
     */
    @Id
    private String productCode;
    private String productType;
    private int price;
    private int providedItemCount;
    private String pricePerItem;
    private String excessChargePerItemCount;
    private String serviceStartDate;
}
