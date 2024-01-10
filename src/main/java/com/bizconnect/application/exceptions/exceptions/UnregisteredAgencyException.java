package com.bizconnect.application.exceptions.exceptions;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import lombok.Getter;

@Getter
public class UnregisteredAgencyException extends RuntimeException {
    private final EnumResultCode enumResultCode;
    private final String siteId;
    public UnregisteredAgencyException(EnumResultCode enumResultCode, String siteId) {
        this.enumResultCode = enumResultCode;
        this.siteId = siteId;
    }
}
