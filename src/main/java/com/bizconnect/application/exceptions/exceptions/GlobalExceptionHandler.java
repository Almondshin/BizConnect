package com.bizconnect.application.exceptions.exceptions;

import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import org.apache.commons.httpclient.Header;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<?> DuplicateMemberException(DuplicateMemberException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.DuplicateMember.getCode(), ex.getMessage(), EnumSiteStatus.UNREGISTERED.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalAgencyIdSiteIdException.class)
    public ResponseEntity<?> IllegalAgencyIdSiteIdException(IllegalAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.IllegalArgument.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(NullAgencyIdSiteIdException.class)
    public ResponseEntity<?> NullAgencyIdSiteIdException(NullAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.NullPointArgument.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> dataExceptionHandle(Exception e) {
//        HttpHeaders headers = new HttpHeaders();
//        HttpStatus httpStatus = HttpStatus.OK;
//        String body;
//        if (e.getMessage().isBlank())
//            body = "요청 실패. 관리자에게 문의해주세요.";
//        else {
//            body = e.getMessage();
//        }
//        e.printStackTrace();
//        return new ResponseEntity<>(body, headers, httpStatus);
//    }
}
