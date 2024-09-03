package com.projectx.expense_service.services;

import com.projectx.expense_service.entity.ExpensesDetails;
import com.projectx.expense_service.exceptions.AlreadyExistsException;
import com.projectx.expense_service.exceptions.InvalidDataException;
import com.projectx.expense_service.exceptions.ResourceNotFoundException;
import com.projectx.expense_service.payloads.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ExpenseService {
    Boolean createExpense(ExpenseDto expenseDto)throws AlreadyExistsException,
            InvalidDataException;
    Boolean updateExpense(ExpenseDto expenseDto)throws AlreadyExistsException,
            ResourceNotFoundException,InvalidDataException;
    ExpensesDetails getById(EntityIdDto entityIdDto)throws ResourceNotFoundException;
    List<ViewExpensesDto> getAllExpensesOfMonths();
    List<ViewExpensesDto> getAllExpenses();
    List<ViewExpenseItemsDto> getExpensesItemsByExpenseId(EntityIdDto entityIdDto)throws ResourceNotFoundException;
    Boolean updateStatus(EntityIdDto entityIdDto)throws ResourceNotFoundException;
    PageResponseDto getAllExpensesPagesWithDateRange(PageRequestDto dto) throws ParseException;
    PageResponseDto getAllExpensesPages(PageRequestDto dto);
    PageResponseDto getMonthlyExpensesPages(MonthlyPageRequestDto dto);
    PageResponseDto getAllExpensesPagesWithDateRangeForReport(DateRangePageRequestDto dto) throws ParseException;
    Integer getMonthlyExpenseCount();
    Integer getAllExpenseCount();
    String getMonthlyExpenseTotal();
    String getYearlyExpenseTotal();
    List<ExpenseInfoDto> getAllExpensesWithDates(DateRangeDto dto);
    ExpenseInfoDto getExpenseInfoById(EntityIdDto dto)throws ResourceNotFoundException;
}
