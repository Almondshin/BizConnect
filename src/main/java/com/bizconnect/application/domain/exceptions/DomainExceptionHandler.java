package com.bizconnect.application.domain.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class DomainExceptionHandler {

//    @ExceptionHandler(CheckedMallIdException.class)
//    public ResponseEntity<ResponseMessage> checkedMallIdException(CheckedMallIdException ex) {
//        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode());
//        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
//    }
//
//
//    @ExceptionHandler(IllegalAgencyIdMallIdException.class)
//    public ResponseEntity<ResponseMessage> IllegalAgencyIdMallIdException(IllegalAgencyIdMallIdException ex) {
//        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode());
//        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
//    }


}
