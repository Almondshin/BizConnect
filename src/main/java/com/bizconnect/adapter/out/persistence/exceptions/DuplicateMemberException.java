package com.bizconnect.adapter.out.persistence.exceptions;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import lombok.Getter;

@Getter
public class DuplicateMemberException extends RuntimeException{
    private final String mallId;
    public DuplicateMemberException(EnumResultCode resultCode, String mallId) {
        super(resultCode.getValue());
        this.mallId = mallId;
    }
}
