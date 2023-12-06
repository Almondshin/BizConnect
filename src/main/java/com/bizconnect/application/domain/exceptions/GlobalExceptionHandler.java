package com.bizconnect.application.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CheckedMallIdException.class)
    public ResponseEntity<ApiError> checkedMallIdException(CheckedMallIdException ex) {
        String message = "Error: " + ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // 다른 예외 처리기들 ...
}
