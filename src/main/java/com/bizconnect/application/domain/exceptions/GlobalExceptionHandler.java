package com.bizconnect.application.domain.exceptions;

import com.bizconnect.application.domain.enums.EnumResultCode;
import com.bizconnect.application.domain.enums.EnumSiteStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CheckedMallIdException.class)
    public ResponseEntity<ResponseMessage> checkedMallIdException(CheckedMallIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalAgencyIdMallIdException.class)
    public ResponseEntity<ResponseMessage> IllegalAgencyIdMallIdException(IllegalAgencyIdMallIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }


}
