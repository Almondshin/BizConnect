package com.bizconnect.adapter.in.model;

import com.bizconnect.paymentmodule.config.hectofinancial.Constant;
import lombok.Getter;

import java.util.Map;

@Getter
public class PaymentDataModel {
    private String mchtId;
    private String method;
    private String mchtTrdNo;
    private String trdDt;
    private String trdTm;
    private String trdAmt;
    private String mchtCustNm;
    private String cphoneNo;
    private String email;
    private String mchtCustId;
    private String taxAmt;
    private String vatAmt;
    private String taxFreeAmt;
    private String svcAmt;
    private String clipCustNm;
    private String clipCustCi;
    private String clipCustPhoneNo;

    public PaymentDataModel() {
    }

    public PaymentDataModel(String mchtId, String method, String mchtTrdNo, String trdDt, String trdTm, String trdAmt) {
        this.mchtId = mchtId;
        this.method = method;
        this.mchtTrdNo = mchtTrdNo;
        this.trdDt = trdDt;
        this.trdTm = trdTm;
        this.trdAmt = trdAmt;
    }

    public PaymentDataModel(String trdAmt, String mchtCustNm, String cphoneNo, String email, String mchtCustId, String taxAmt, String vatAmt, String taxFreeAmt, String svcAmt, String clipCustNm, String clipCustCi, String clipCustPhoneNo) {
        this.trdAmt = trdAmt;
        this.mchtCustNm = mchtCustNm;
        this.cphoneNo = cphoneNo;
        this.email = email;
        this.mchtCustId = mchtCustId;
        this.taxAmt = taxAmt;
        this.vatAmt = vatAmt;
        this.taxFreeAmt = taxFreeAmt;
        this.svcAmt = svcAmt;
        this.clipCustNm = clipCustNm;
        this.clipCustCi = clipCustCi;
        this.clipCustPhoneNo = clipCustPhoneNo;
    }

    public PaymentDataModel(Map<String, String> requestMap) {
    }
}
