package com.bizconnect.adapter.in.exceptions;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import com.bizconnect.adapter.in.enums.EnumSiteStatus;
import lombok.Getter;

@Getter
public class ClientResponseMessage {
    private final String resultCode;
    private final String message;
    private final String siteStatus;
    private final String mallId;

    public ClientResponseMessage(String resultCode, String message, String statusCode, String mallId) {
        this.resultCode = EnumResultCode.fromCode(resultCode);
        this.message = message;
        this.siteStatus = EnumSiteStatus.fromCode(statusCode);
        this.mallId = mallId;
    }
}