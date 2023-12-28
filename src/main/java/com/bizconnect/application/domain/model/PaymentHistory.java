package com.bizconnect.application.domain.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class PaymentHistory {
    private String tradeNum;
    private String hfTradeNum;
    private String agencyId;
    private String siteId;

    private String paymentType;
    private String rateSel;
    private String amount;
    private String offer;
    private String useCount;

    private Date trDate;
    private Date startDate;
    private Date endDate;

    private String rcptName;
    private String paymentStatus;

    private String vbankName;
    private String vbankCode;
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

    public PaymentHistory(String tradeNum, String hfTradeNum,String agencyId, String siteId, String paymentType, String rateSel, String amount, String offer, Date trDate, Date startDate, Date endDate, String paymentStatus, Date regDate) {
        this.tradeNum = tradeNum;
        this.hfTradeNum = hfTradeNum;
        this.agencyId = agencyId;
        this.siteId = siteId;
        this.paymentType = paymentType;
        this.rateSel = rateSel;
        this.amount = amount;
        this.offer = offer;
        this.trDate = trDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentStatus = paymentStatus;
        this.regDate = regDate;
    }

    public PaymentHistory(String tradeNum, String hfTradeNum,String agencyId, String siteId, String paymentType, String rateSel, String amount, String offer, Date trDate, String rcptName, String paymentStatus, String vbankName, String vbankCode, String vbankAccount, String vbankExpireDate, Date startDate, Date endDate, Date regDate) {
        this.tradeNum = tradeNum;
        this.hfTradeNum = hfTradeNum;
        this.agencyId = agencyId;
        this.siteId = siteId;
        this.paymentType = paymentType;
        this.rateSel = rateSel;
        this.amount = amount;
        this.offer = offer;
        this.trDate = trDate;
        this.rcptName = rcptName;
        this.paymentStatus = paymentStatus;
        this.vbankName = vbankName;
        this.vbankCode = vbankCode;
        this.vbankAccount = vbankAccount;
        this.vbankExpireDate = vbankExpireDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regDate = regDate;
    }
}
