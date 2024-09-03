package com.projectx.expense_service.utils;

import com.projectx.expense_service.payloads.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

@Component
public class ErrorHandlerComponent {

    @Autowired
    private MessageSource messageSource;

    public <T> ResponseEntity<ResponseDto<T>> handleValidationErrors(BindingResult result) {

        Map<String, String> errorMap = new HashMap<>();

        Set<String> errorMsgList = new HashSet<>();

        List<FieldError> fieldErrorList = result.getFieldErrors();

        List<ObjectError> objectErrorList = result.getAllErrors();

        for (FieldError fieldError : fieldErrorList) {
            errorMap.put(fieldError.getField(), getMessageLocal(fieldError.getDefaultMessage()));
            errorMsgList.add(fieldError.getDefaultMessage());
        }

        for (ObjectError objectError : objectErrorList) {
            if (!errorMsgList.contains(objectError.getDefaultMessage()))
                errorMap.put("common", getMessageLocal(objectError.getDefaultMessage()));
        }
        return new ResponseEntity<>(new ResponseDto<>(errorMap), HttpStatus.NOT_ACCEPTABLE);
    }

    public <T> ResponseEntity<ResponseDto<T>> handleError(Exception e) {
        return new ResponseEntity<>(new ResponseDto<>(getMessageLocal(e.getLocalizedMessage())), HttpStatus.OK);
    }

    public String getMessageLocal(String message) {
        if (Objects.isNull(message)) {
            return "An unexpected error has occurred("+message+")";
        }
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, null, locale);
    }
}
