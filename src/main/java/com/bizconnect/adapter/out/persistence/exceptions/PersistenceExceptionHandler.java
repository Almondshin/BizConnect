package com.bizconnect.adapter.out.persistence.exceptions;

import com.bizconnect.adapter.out.persistence.enums.EnumResultCode;
import com.bizconnect.adapter.out.persistence.enums.EnumSiteStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PersistenceExceptionHandler {
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<PersistenceResponseMessage> DuplicateMemberException(DuplicateMemberException ex) {
        PersistenceResponseMessage responseMessage = new PersistenceResponseMessage(EnumResultCode.DuplicateMember.getCode(), ex.getMessage(), EnumSiteStatus.UNREGISTERED.getCode(), ex.getMallId());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<PersistenceResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorsStringBuilder = new StringBuilder();
        String firstFieldName = null;

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;

                if (firstFieldName == null) {
                    firstFieldName = fieldError.getField(); // 첫 번째 필드 이름 저장
                }

                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                errorsStringBuilder.append(fieldName).append(": ").append(errorMessage).append("; ");
            }
        }

        String allErrorMessages = errorsStringBuilder.toString();

        // PersistenceResponseMessage 객체 생성
        PersistenceResponseMessage responseMessage = new PersistenceResponseMessage(
                EnumResultCode.NullPointer.getCode(),
                allErrorMessages, // 모든 필드의 오류 메시지 사용
                EnumSiteStatus.UNREGISTERED.getCode(),
                firstFieldName    // 첫 번째 필드 이름 사용
        );

        // 최종 응답 반환
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }


}
