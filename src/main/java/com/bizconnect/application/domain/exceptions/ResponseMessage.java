package com.bizconnect.application.domain.exceptions;

import com.bizconnect.application.domain.enums.EnumResultCode;
import com.bizconnect.application.domain.enums.EnumSiteStatus;
import lombok.Getter;

@Getter
public class ResponseMessage {

    private final String resultCode;
    private final String message;
    private final String siteStatus;

    public ResponseMessage(String resultCode, String message, String statusCode) {
        this.resultCode = EnumResultCode.fromCode(resultCode);
        this.message = message;
        this.siteStatus = EnumSiteStatus.fromCode(statusCode);
    }
}