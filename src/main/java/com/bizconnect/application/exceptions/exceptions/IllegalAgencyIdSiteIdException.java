package com.bizconnect.application.exceptions.exceptions;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import lombok.Getter;

@Getter
public class IllegalAgencyIdSiteIdException extends RuntimeException {
    private final String siteId;
    public IllegalAgencyIdSiteIdException(EnumResultCode resultCode, String siteId) {
        super(resultCode.getValue());
        this.siteId = siteId;
    }


}
