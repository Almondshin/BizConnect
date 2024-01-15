package com.bizconnect.application.exceptions.exceptions.handler;

import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.*;
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
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<?> DuplicateMemberException(DuplicateMemberException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.DuplicateMember.getCode(), EnumResultCode.DuplicateMember.getValue());
        logger.info("S ------------------------------[Exception] - [DuplicateMemberException] ------------------------------ S");
        logger.info("[Exception siteId] : [" + ex.getSiteId() + "]");
        logger.info("[Exception ResultCode] : [" + responseMessage.getResultCode() + "]");
        logger.info("[Exception ResultMsg] : [" + responseMessage.getResultMsg() + "]");
        logger.info("E ------------------------------[Exception] - [DuplicateMemberException] ------------------------------ E");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    @ExceptionHandler(IllegalAgencyIdSiteIdException.class)
    public ResponseEntity<?> IllegalAgencyIdSiteIdException(IllegalAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.IllegalArgument.getCode(), EnumResultCode.IllegalArgument.getValue());
        logger.info("S ------------------------------[Exception] - [IllegalAgencyIdSiteIdException] ------------------------------ S");
        logger.info("[Exception siteId] : [" + ex.getSiteId() + "]");
        logger.info("[Exception ResultCode] : [" + responseMessage.getResultCode() + "]");
        logger.info("[Exception ResultMsg] : [" + responseMessage.getResultMsg() + "]");
        logger.info("E ------------------------------[Exception] - [IllegalAgencyIdSiteIdException] ------------------------------ E");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    @ExceptionHandler(UnregisteredAgencyException.class)
    public ResponseEntity<?> UnregisteredAgencyException(UnregisteredAgencyException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.UnregisteredAgency.getCode(), EnumResultCode.UnregisteredAgency.getValue());
        logger.info("S ------------------------------[Exception] - [UnregisteredAgencyException] ------------------------------ S");
        logger.info("[Exception siteId] : [" + ex.getSiteId() + "]");
        logger.info("[Exception ResultCode] : [" + responseMessage.getResultCode() + "]");
        logger.info("[Exception ResultMsg] : [" + responseMessage.getResultMsg() + "]");
        logger.info("E ------------------------------[Exception] - [UnregisteredAgencyException] ------------------------------ E");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(NullAgencyIdSiteIdException.class)
    public ResponseEntity<?> NullAgencyIdSiteIdException(NullAgencyIdSiteIdException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.NullPointArgument.getCode(), EnumResultCode.NullPointArgument.getValue());
        logger.info("S ------------------------------[Exception] - [NullAgencyIdSiteIdException] ------------------------------ S");
        logger.info("[Exception siteId] : [" + ex.getSiteId() + "]");
        logger.info("[Exception ResultCode] : [" + responseMessage.getResultCode() + "]");
        logger.info("[Exception ResultMsg] : [" + responseMessage.getResultMsg() + "]");
        logger.info("E ------------------------------[Exception] - [NullAgencyIdSiteIdException] ------------------------------ E");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(NoExtensionException.class)
    public ResponseEntity<?> NoExtensionException(NoExtensionException ex) {
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.NoExtension.getCode(), EnumResultCode.NoExtension.getValue());
        logger.info("S ------------------------------[Exception] - [NoExtensionException] ------------------------------ S");
        logger.info("[Exception siteId] : [" + ex.getSiteId() + "]");
        logger.info("[Exception ResultCode] : [" + responseMessage.getResultCode() + "]");
        logger.info("[Exception ResultMsg] : [" + responseMessage.getResultMsg() + "]");
        logger.info("E ------------------------------[Exception] - [NoExtensionException] ------------------------------ E");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
