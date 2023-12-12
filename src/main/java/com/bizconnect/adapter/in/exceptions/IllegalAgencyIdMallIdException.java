package com.bizconnect.adapter.in.exceptions;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import lombok.Getter;

@Getter
public class IllegalAgencyIdMallIdException extends RuntimeException {
    private final String mallId;
    public IllegalAgencyIdMallIdException(EnumResultCode resultCode, String mallId) {
        super(resultCode.getValue());
        this.mallId = mallId;
    }
}
