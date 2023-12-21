package com.bizconnect.application.exceptions.exceptions;

import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ResponseMessage> DuplicateMemberException(DuplicateMemberException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.DuplicateMember.getCode(), ex.getMessage(), EnumSiteStatus.UNREGISTERED.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalAgencyIdSiteIdException.class)
    public ResponseEntity<ResponseMessage> IllegalAgencyIdSiteIdException(IllegalAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.IllegalArgument.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullAgencyIdSiteIdException.class)
    public ResponseEntity<ResponseMessage> NullAgencyIdSiteIdException(NullAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.NullPointArgument.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
