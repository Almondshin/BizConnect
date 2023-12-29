package com.bizconnect.application.exceptions.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ValueException extends RuntimeException{
    private final int offer;
    private final int cliOffer;
    private final int price;
    private final int cliPrice;
    private final String agencyId;
    private final String siteId;

    public ValueException(int offer, int cliOffer, int price, int cliPrice, String agencyId, String siteId) {
        this.offer = offer;
        this.cliOffer = cliOffer;
        this.price = price;
        this.cliPrice = cliPrice;
        this.agencyId = agencyId;
        this.siteId = siteId;
    }
}
