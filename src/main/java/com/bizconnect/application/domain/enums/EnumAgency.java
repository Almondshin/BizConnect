package com.bizconnect.application.domain.enums;

import lombok.Getter;

@Getter
public enum EnumAgency {
    SQUARES("squares", "스퀘어스", "SiteStatus", "RegAgencySiteInfo", "CancelSiteInfo", "NotifyPaymentSiteInfo","NotifyStatusSite"),
    DREAMTEST("dreamtest", "드림테스트", "SiteStatus", "RegAgencySiteInfo", "CancelSiteInfo", "NotifyPaymentSiteInfo","NotifyStatusSite"),
    CAFE24("CAFE24" , "카페24","", "","","","");

    private final String code;
    private final String value;
    private final String statusMsg;
    private final String regMsg;
    private final String cancelMsg;
    private final String paymentMsg;
    private final String siteStatusMsg;

    EnumAgency(String code, String value, String statusMsg, String regMsg, String cancelMsg, String paymentMsg, String siteStatusMsg) {
        this.code = code;
        this.value = value;
        this.statusMsg = statusMsg;
        this.regMsg = regMsg;
        this.cancelMsg = cancelMsg;
        this.paymentMsg = paymentMsg;
        this.siteStatusMsg = siteStatusMsg;
    }

}
