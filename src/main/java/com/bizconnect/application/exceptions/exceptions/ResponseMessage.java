package com.bizconnect.application.exceptions.exceptions;

import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import lombok.Getter;

@Getter
public class ResponseMessage {
    private final String resultCode;
    private final String message;
    private final String siteStatus;
    private final String siteId;

    public ResponseMessage(String resultCode, String message, String statusCode, String siteId) {
        this.resultCode = EnumResultCode.fromCode(resultCode);
        this.message = message;
        this.siteStatus = EnumSiteStatus.fromCode(statusCode);
        this.siteId = siteId;
    }
}