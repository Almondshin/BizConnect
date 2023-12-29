package com.bizconnect.application.exceptions.exceptions.handler;

import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import com.bizconnect.application.exceptions.exceptions.DuplicateMemberException;
import com.bizconnect.application.exceptions.exceptions.IllegalAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.ValueException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<?> DuplicateMemberException(DuplicateMemberException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.DuplicateMember.getCode(), ex.getMessage(), EnumSiteStatus.DUPLICATE.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    @ExceptionHandler(IllegalAgencyIdSiteIdException.class)
    public ResponseEntity<?> IllegalAgencyIdSiteIdException(IllegalAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.IllegalArgument.getCode(), ex.getMessage(), EnumSiteStatus.UNREGISTERED.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    @ExceptionHandler(NullAgencyIdSiteIdException.class)
    public ResponseEntity<?> NullAgencyIdSiteIdException(NullAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.NullPointArgument.getCode(), ex.getMessage(), EnumSiteStatus.UNREGISTERED.getCode(), ex.getSiteId());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(ValueException.class)
    public void handleValueException(ValueException ex) {
        log.error("Price or Offer doesn't match: " + ex.toString());
        // 여기서 추가적인 로그 메시지나 처리를 추가할 수 있습니다.
    }
}
