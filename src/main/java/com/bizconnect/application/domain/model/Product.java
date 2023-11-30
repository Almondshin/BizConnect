package com.bizconnect.application.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Product {
    /* 결제 상품 */
    /*
    상품코드 (Product Code) : productCode
    상품종류 (Product Type): productType
    요금 (Price): price
    제공건수 (Number of Provided Items): providedItemCount
    건당요금 (Price Per Item): pricePerItem
    건당 초과 요금 (Excess Charge Per Item): excessChargePerItem
    서비스 시작일 (Service Start Date) serviceStartDate
    서비스 종료일 (Service End Date) serviceEndDate
    */
    private String productCode;
    private String productType;
    private int price;
    private int providedItemCount;
    private String pricePerItem;
    private String excessChargePerItemCount;
    private LocalDateTime serviceStartDate;
    private LocalDateTime serviceEndDate;
}
