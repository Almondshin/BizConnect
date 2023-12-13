package com.bizconnect.adapter.out.persistence.exceptions;

import com.bizconnect.adapter.out.persistence.enums.EnumResultCode;
import com.bizconnect.adapter.out.persistence.enums.EnumSiteStatus;
import lombok.Getter;

@Getter
public class PersistenceResponseMessage {
    private final String resultCode;
    private final String message;
    private final String siteStatus;
    private final String mallId;

    public PersistenceResponseMessage(String resultCode, String message, String statusCode, String mallId) {
        this.resultCode = EnumResultCode.fromCode(resultCode);
        this.message = message;
        this.siteStatus = EnumSiteStatus.fromCode(statusCode);
        this.mallId = mallId;
    }
}