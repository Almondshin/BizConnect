package com.bizconnect.adapter.in.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PaymentHistoryDataModel {
    private String tradeNum;
    private String pgTradeNum;
    private String agencyId;
    private String siteId;

    private String paymentType;
    private String rateSel;
    private String amount;
    private String offer;
    private int useCount;
    private String paymentStatus;

    private Date trDate;
    private Date startDate;
    private Date endDate;

    private String rcptName;

    private String vbankName;
    private String vbankAccount;
    private Date vbankExpireDate;

    private Date regDate;
    private Date modDate;
    private String memo;
}
