package com.bizconnect.application.domain.enums;

import lombok.Getter;

@Getter
public enum EnumAgency {
    SQUARES("SQUARES", "스퀘어스","RegAgencySiteInfo", "CancelSiteInfo" ),
    CAFE24("CAFE24" , "카페24", "","");

    private final String code;
    private final String value;
    private final String regMsg;
    private final String cancelMsg;

    EnumAgency(String code, String value, String regMsg, String cancelMsg) {
        this.code = code;
        this.value = value;
        this.regMsg = regMsg;
        this.cancelMsg = cancelMsg;
    }

}
