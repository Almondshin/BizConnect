package com.bizconnect.adapter.out.persistence.exceptions;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import com.bizconnect.adapter.in.enums.EnumSiteStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersistenceExceptionHandler {
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<PersistenceResponseMessage> DuplicateMemberException(DuplicateMemberException ex) {
        PersistenceResponseMessage responseMessage = new PersistenceResponseMessage(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode(), ex.getMallId());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
}
