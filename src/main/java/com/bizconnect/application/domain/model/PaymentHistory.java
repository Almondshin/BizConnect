package com.bizconnect.application.domain.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class PaymentHistory {
    private String tradeNum;
    private String agencyId;
    private String siteId;

    private String paymentType;
    private String rateSel;
    private String amount;
    private String offer;
    private int useCount;

    private Date trDate;
    private Date startDate;
    private Date endDate;

    private String rcptName;
    private String paymentStatus;

    private String vbankName;
    private String vbankAccount;
    private String vbankExpireDate;

    private Date regDate;
    private Date modDate;
    private String memo;


    public PaymentHistory(String agencyId, String siteId, Date startDate, Date endDate, String salesPrice, String rateSel, String method) {
        this.agencyId = agencyId;
        this.siteId = siteId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = salesPrice;
        this.rateSel = rateSel;
        this.paymentType = method;
    }
}
