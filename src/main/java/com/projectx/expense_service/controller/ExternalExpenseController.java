package com.projectx.expense_service.controller;

import com.projectx.expense_service.exceptions.ResourceNotFoundException;
import com.projectx.expense_service.payloads.*;
import com.projectx.expense_service.services.ExternalExpenseService;
import com.projectx.expense_service.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/externalExpenses")
public class ExternalExpenseController {

    @Autowired
    private ExternalExpenseService externalExpenseService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/addUpdate")
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(
            @RequestBody @Valid ExternalExpenseDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = externalExpenseService.addUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | ParseException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<ExternalExpenseDto>> getById(
            @RequestBody @Valid EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ExternalExpenseDto data = externalExpenseService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/deleteById")
    public ResponseEntity<ResponseDto<Boolean>> deleteById(
            @RequestBody @Valid EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = externalExpenseService.deleteById(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(
            @RequestBody @Valid EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = externalExpenseService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getAllExternalExpensesPages")
    public ResponseEntity<ResponseDto<ExternalExpensePageResponseDto>> getAllExternalExpensesPages(
            @RequestBody @Valid PageRequestDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ExternalExpensePageResponseDto data = externalExpenseService.getAllExternalExpensesPages(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
