package com.projectx.expense_service.services;

import com.projectx.expense_service.exceptions.ResourceNotFoundException;
import com.projectx.expense_service.payloads.EntityIdDto;
import com.projectx.expense_service.payloads.ExternalExpenseDto;
import com.projectx.expense_service.payloads.ExternalExpensePageResponseDto;
import com.projectx.expense_service.payloads.PageRequestDto;

import java.text.ParseException;

public interface ExternalExpenseService {
    Boolean addUpdate(ExternalExpenseDto dto) throws ResourceNotFoundException, ParseException;
    ExternalExpenseDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    ExternalExpensePageResponseDto getAllExternalExpensesPages(PageRequestDto dto);
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    Boolean deleteById(EntityIdDto dto)throws ResourceNotFoundException;
}
