package com.bizconnect.application.exceptions.exceptions;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import lombok.Getter;

@Getter
public class NullAgencyIdSiteIdException extends RuntimeException {
    private final String siteId;
    public NullAgencyIdSiteIdException(EnumResultCode resultCode, String siteId) {
        super(resultCode.getValue());
        this.siteId = siteId;
    }


}
