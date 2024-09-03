package com.projectx.expense_service.payloads;

import java.util.Map;

public class ResponseDto<T> {
    private T result;
    private String errorMessage;
    private Map<String,String> validationMessages;

    public ResponseDto(){

    }

    public ResponseDto(T result, String errorMessage) {
        super();
        this.result = result;
        this.errorMessage = errorMessage;
    }
    public ResponseDto(T result, String errorMessage, Map<String ,String> validationMessages) {
        super();
        this.result=result;
        this.errorMessage=errorMessage;
        this.validationMessages=validationMessages;
    }
    public ResponseDto(T result) {
        super();
        this.result=result;
        this.errorMessage="";
    }
    public ResponseDto(String errorMessage){
        super();
        this.errorMessage=errorMessage;
    }
    public ResponseDto(Map<String, String> validationMessages) {
        super();
        this.validationMessages=validationMessages;
        this.errorMessage="";
    }

    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, String> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(Map<String, String> validationMessages) {
        this.validationMessages = validationMessages;
    }
}
