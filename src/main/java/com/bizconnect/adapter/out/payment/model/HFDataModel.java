package com.bizconnect.adapter.out.payment.model;

import lombok.Getter;

@Getter
public class HFDataModel {
    private String mchtTrdNo;
    private String pointTrdNo;
    private String trdNo;
    private String vtlAcntNo;
    private String mchtCustId;
    private String fnNm;
    private String method;
    private String authNo;
    private String trdAmt;
    private String pointTrdAmt;
    private String cardTrdAmt;
    private String outRsltMsg;
    private String mchtParam;
    private String outStatCd;
    private String outRsltCd;
    private String intMon;
    private String authDt;
    private String mchtId;
    private String fnCd;
    private String expireDt;


    private String bizType;
    private String mchtCustNm;
    private String mchtName;
    private String pmtprdNm;
    private String trdDtm;
    private String bankCd;
    private String bankNm;
    private String acntType;
    private String vAcntNo;
    private String AcntPrintNm;
    private String dpstrNm;
    private String email;
    private String orgTrdNo;
    private String orgTrdDt;
    private String csrcIssNo;
    private String cnclType;
    private String pktHash;

    private String billKey;
    private String billKeyExpireDt;
    private String cardCd;
    private String cardNm;
    private String telecomCd;
    private String telecomNm;
    private String cardNo;
    private String cardApprNo;
    private String instmtMon;
    private String instmtType;
    private String phoneNoEnc;
    private String mixTrdNo;
    private String mixTrdAmt;
    private String payAmt;

    public HFDataModel(String mchtTrdNo, String trdAmt, String outStatCd, String mchtId, String trdDtm) {
        this.mchtTrdNo = mchtTrdNo;
        this.trdAmt = trdAmt;
        this.outStatCd = outStatCd;
        this.mchtId = mchtId;
        this.trdDtm = trdDtm;
    }

    public String getHashPlain(){
        return this.outStatCd+this.trdDtm+this.mchtId+this.mchtTrdNo+this.trdAmt;
    }

}
