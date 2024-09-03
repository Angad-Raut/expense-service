package com.projectx.expense_service.controller;

import com.projectx.expense_service.entity.ExpensesDetails;
import com.projectx.expense_service.exceptions.AlreadyExistsException;
import com.projectx.expense_service.exceptions.InvalidDataException;
import com.projectx.expense_service.exceptions.ResourceNotFoundException;
import com.projectx.expense_service.payloads.*;
import com.projectx.expense_service.services.ExpenseService;
import com.projectx.expense_service.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/createExpense")
    public ResponseEntity<ResponseDto<Boolean>> createExpense(
            @RequestBody ExpenseDto expenseDto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = expenseService.createExpense(expenseDto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.CREATED);
        } catch (AlreadyExistsException | InvalidDataException e) {
            return errorHandler.handleError(e);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateExpense")
    public ResponseEntity<ResponseDto<Boolean>> updateExpense(
            @RequestBody @Valid ExpenseDto expenseDto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = expenseService.updateExpense(expenseDto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (AlreadyExistsException | InvalidDataException | ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getExpenseById")
    public ResponseEntity<ResponseDto<ExpensesDetails>> getExpenseById(
            @RequestBody @Valid EntityIdDto entityIdDto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ExpensesDetails data = expenseService.getById(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateExpenseStatusById")
    public ResponseEntity<ResponseDto<Boolean>> updateExpenseStatusById(
            @RequestBody @Valid EntityIdDto entityIdDto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = expenseService.updateStatus(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getAllExpensesOfMonth")
    public ResponseEntity<ResponseDto<List<ViewExpensesDto>>> getAllExpensesOfMonth() {
        try {
            List<ViewExpensesDto> result = expenseService.getAllExpensesOfMonths();
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @GetMapping(value = "/getAllExpenses")
    public ResponseEntity<ResponseDto<List<ViewExpensesDto>>> getAllExpenses() {
        try {
            List<ViewExpensesDto> result = expenseService.getAllExpenses();
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getExpenseItemsByExpenseId")
    public ResponseEntity<ResponseDto<List<ViewExpenseItemsDto>>> getExpenseItemsByExpenseId(
            @RequestBody @Valid EntityIdDto entityIdDto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<ViewExpenseItemsDto> data = expenseService.getExpensesItemsByExpenseId(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getAllExpensesPagesWithDateRange")
    public ResponseEntity<ResponseDto<PageResponseDto>> getAllExpensesPagesWithDateRange(
            @Valid @RequestBody PageRequestDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            PageResponseDto data = expenseService.getAllExpensesPagesWithDateRange(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }
    @PostMapping(value = "/getAllExpensesPagesWithDateRangeForReport")
    public ResponseEntity<ResponseDto<PageResponseDto>> getAllExpensesPagesWithDateRangeForReport(
            @Valid @RequestBody DateRangePageRequestDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            PageResponseDto data = expenseService.getAllExpensesPagesWithDateRangeForReport(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getAllExpensesPages")
    public ResponseEntity<ResponseDto<PageResponseDto>> getAllExpensesPages(
            @Valid @RequestBody PageRequestDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            PageResponseDto data = expenseService.getAllExpensesPages(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getMonthlyExpensesPages")
    public ResponseEntity<ResponseDto<PageResponseDto>> getMonthlyExpensesPages(
            @Valid @RequestBody MonthlyPageRequestDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            PageResponseDto data = expenseService.getMonthlyExpensesPages(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getAllExpensesWithDates")
    public ResponseEntity<ResponseDto<List<ExpenseInfoDto>>> getAllExpensesWithDates(
            @Valid @RequestBody DateRangeDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<ExpenseInfoDto> data = expenseService.getAllExpensesWithDates(dto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getExpenseInfoById")
    public ResponseEntity<ResponseDto<ExpenseInfoDto>> getExpenseInfoById(
            @RequestBody @Valid EntityIdDto entityIdDto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ExpenseInfoDto data = expenseService.getExpenseInfoById(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(
                    data,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
