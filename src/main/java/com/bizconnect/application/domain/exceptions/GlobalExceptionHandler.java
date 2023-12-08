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
    public ResponseEntity<ApiError> checkedMallIdException(CheckedMallIdException ex) {
        ApiError apiError = new ApiError(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAgencyIdMallIdException.class)
    public ResponseEntity<ApiError> IllegalAgencyIdMallIdException(IllegalAgencyIdMallIdException ex) {
        ApiError apiError = new ApiError(EnumResultCode.FAIL.getCode(), ex.getMessage(), EnumSiteStatus.ACTIVE.getCode());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
