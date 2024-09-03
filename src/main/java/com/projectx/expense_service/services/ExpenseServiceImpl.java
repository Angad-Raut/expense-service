package com.projectx.expense_service.services;

import com.projectx.expense_service.entity.ExpenseItems;
import com.projectx.expense_service.entity.ExpensesDetails;
import com.projectx.expense_service.exceptions.AlreadyExistsException;
import com.projectx.expense_service.exceptions.InvalidDataException;
import com.projectx.expense_service.exceptions.ResourceNotFoundException;
import com.projectx.expense_service.payloads.*;
import com.projectx.expense_service.repository.ExpensesRepository;
import com.projectx.expense_service.utils.ExpenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Override
    public Boolean createExpense(ExpenseDto expenseDto)
            throws AlreadyExistsException, InvalidDataException {
        try {
            isExpenseExist();
            Set<ExpenseItemDto> expenseItemDtos = expenseDto.getExpenseItemDtos();
            ExpensesDetails expensesDetails = ExpensesDetails.builder()
                    .expenseItems(expenseItemDtos.stream()
                            .map(data -> ExpenseItems.builder()
                                    .itemName(data.getItemName())
                                    .itemPrice(data.getItemPrice())
                                    .paymentType(data.getPaymentType())
                                    .build())
                            .collect(Collectors.toSet()))
                    .totalAmount(expenseItemDtos.stream()
                            .map(ExpenseItemDto::getItemPrice)
                            .mapToDouble(Double::doubleValue).sum())
                    .insertedTime(expenseDto.getExpenseDate()!=null?ExpenseUtils.getISODate(expenseDto.getExpenseDate()):new Date())
                    .status(true)
                    .build();
            return expensesRepository.save(expensesDetails)!=null?true:false;
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public Boolean updateExpense(ExpenseDto expenseDto)
            throws AlreadyExistsException, ResourceNotFoundException, InvalidDataException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(expenseDto.getId());
            if (details==null) {
                throw new ResourceNotFoundException(ExpenseUtils.EXPENSE_NOT_EXISTS);
            }
            Set<ExpenseItems> expenseItems = setItems(expenseDto.getExpenseItemDtos(),details.getExpenseItems());
            details.setExpenseItems(expenseItems);
            details.setTotalAmount(expenseItems.stream()
                    .map(ExpenseItems::getItemPrice)
                    .mapToDouble(Double::doubleValue).sum());
            return expensesRepository.save(details)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public ExpensesDetails getById(EntityIdDto entityIdDto)
            throws ResourceNotFoundException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(entityIdDto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(ExpenseUtils.EXPENSE_NOT_EXISTS);
            }
            return details;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewExpensesDto> getAllExpensesOfMonths() {
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpensesWithDates(ExpenseUtils.firstDayOfMonth(),ExpenseUtils.lastDayOfMonth());
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId()!=null?data.getId():null)
                        .totalAmount(data.getTotalAmount()!=null?ExpenseUtils.toINRFormat(data.getTotalAmount()):ExpenseUtils.DASH)
                        .expenseDate(data.getInsertedTime()!=null?ExpenseUtils.toExpenseDate(data.getInsertedTime()):ExpenseUtils.DASH)
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<ViewExpensesDto> getAllExpenses() {
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpenses();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId()!=null?data.getId():null)
                        .totalAmount(data.getTotalAmount()!=null?ExpenseUtils.toINRFormat(data.getTotalAmount()):ExpenseUtils.DASH)
                        .expenseDate(data.getInsertedTime()!=null?ExpenseUtils.toExpenseDate(data.getInsertedTime()):ExpenseUtils.DASH)
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<ViewExpenseItemsDto> getExpensesItemsByExpenseId(EntityIdDto entityIdDto)
            throws ResourceNotFoundException {
        ExpensesDetails expensesDetails = expensesRepository.getExpensesById(entityIdDto.getEntityId());
        if (expensesDetails!=null) {
            List<Object[]> fetchList = expensesRepository.getExpenseItemsByExpenseId(entityIdDto.getEntityId());
            AtomicInteger index = new AtomicInteger(0);
            return !fetchList.isEmpty() ? fetchList.stream()
                    .map(data -> ViewExpenseItemsDto.builder()
                            .srNo(index.incrementAndGet())
                            .itemName(data[0] != null ? data[0].toString():ExpenseUtils.DASH)
                            .itemPrice(data[1] != null ? ExpenseUtils.toINRFormat(Double.parseDouble(data[1].toString())) : ExpenseUtils.DASH)
                            .paymentWith(data[2] != null ? data[2].toString() : ExpenseUtils.DASH)
                            .build())
                    .collect(Collectors.toList()) : new ArrayList<>();
        } else {
            throw new ResourceNotFoundException(ExpenseUtils.EXPENSE_NOT_EXISTS);
        }
    }

    @Override
    public Boolean updateStatus(EntityIdDto entityIdDto)
            throws ResourceNotFoundException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(entityIdDto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(ExpenseUtils.EXPENSE_NOT_EXISTS);
            }
            Boolean status = details.getStatus()!=null && details.getStatus()?false:true;
            Integer count = expensesRepository.updateStatus(entityIdDto.getEntityId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public PageResponseDto getAllExpensesPagesWithDateRange(PageRequestDto dto) throws ParseException {
        Date startDate = ExpenseUtils.firstDayOfMonth();
        Date endDate = ExpenseUtils.lastDayOfMonth();
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPagesWithDateRange(
                startDate,endDate,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId())
                        .expenseDate(ExpenseUtils.toExpenseDate(data.getInsertedTime()))
                        .totalAmount(data.getTotalAmount()!=null?ExpenseUtils.toINRFormat(data.getTotalAmount()):ExpenseUtils.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public PageResponseDto getAllExpensesPages(PageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPages(pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId())
                        .expenseDate(ExpenseUtils.toExpenseDate(data.getInsertedTime()))
                        .totalAmount(data.getTotalAmount()!=null?ExpenseUtils.toINRFormat(data.getTotalAmount()):ExpenseUtils.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public PageResponseDto getMonthlyExpensesPages(MonthlyPageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        String data[] = dto.getMonthName().split(" ");
        LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(data[1].toString()), Month.valueOf(data[0].toString().toUpperCase()),1,0,0,0,0);
        LocalDateTime startDate = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).plusHours(0).plusMinutes(0).plusSeconds(0);
        LocalDateTime endDate = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59);
        Date fromDate = Date.from(startDate.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth()).toInstant());
        Date toDate = Date.from(endDate.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth()).toInstant());
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPagesWithDateRange(
                fromDate,toDate,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(result -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(result.getId())
                        .expenseDate(ExpenseUtils.toExpenseDate(result.getInsertedTime()))
                        .totalAmount(result.getTotalAmount()!=null?ExpenseUtils.toINRFormat(result.getTotalAmount()):ExpenseUtils.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public PageResponseDto getAllExpensesPagesWithDateRangeForReport(DateRangePageRequestDto dto) throws ParseException {
        Date startDate = ExpenseUtils.getISOStartDate(dto.getStartDate());
        Date endDate = ExpenseUtils.getISOEndDate(dto.getEndDate());
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPagesWithDateRange(
                startDate,endDate,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId())
                        .expenseDate(ExpenseUtils.toExpenseDate(data.getInsertedTime()))
                        .totalAmount(data.getTotalAmount()!=null?ExpenseUtils.toINRFormat(data.getTotalAmount()):ExpenseUtils.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public Integer getMonthlyExpenseCount() {
        Integer monthlyExpenseCount = expensesRepository.expenseExists(ExpenseUtils.firstDayOfMonth(),ExpenseUtils.lastDayOfMonth());
        return monthlyExpenseCount!=null?monthlyExpenseCount:0;
    }

    @Override
    public Integer getAllExpenseCount() {
        Integer allExpenseCount = expensesRepository.getAllExpenseCount();
        return allExpenseCount!=null?allExpenseCount:0;
    }

    @Override
    public String getMonthlyExpenseTotal() {
        Double monthlyExpenseTotal = expensesRepository.getTotalAmountSum(ExpenseUtils.firstDayOfMonth(),ExpenseUtils.lastDayOfMonth());
        return monthlyExpenseTotal!=null?setTotalAmountSum(monthlyExpenseTotal):setTotalAmountSum(0.0);
    }

    @Override
    public String getYearlyExpenseTotal() {
        Double yearlyExpenseTotal = expensesRepository.getTotalAmountSum(ExpenseUtils.firstDayOfYear(),ExpenseUtils.lastDayOfYear());
        return yearlyExpenseTotal!=null?setTotalAmountSum(yearlyExpenseTotal):setTotalAmountSum(0.0);
    }

    @Override
    public List<ExpenseInfoDto> getAllExpensesWithDates(DateRangeDto dto) {
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpensesWithDates(dto.getStartDate(),dto.getEndDate());
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ExpenseInfoDto.builder()
                        .id(data.getId())
                        .totalAmount(data.getTotalAmount())
                        .status(data.getStatus())
                        .insertedTime(data.getInsertedTime())
                        .expenseItems(setItemsDto(data.getExpenseItems()))
                        .build())
                .toList():new ArrayList<>();
    }

    @Override
    public ExpenseInfoDto getExpenseInfoById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(ExpenseUtils.EXPENSE_NOT_EXISTS);
            }
            return ExpenseInfoDto.builder()
                    .id(details.getId())
                    .totalAmount(details.getTotalAmount())
                    .status(details.getStatus())
                    .insertedTime(details.getInsertedTime())
                    .expenseItems(setItemsDto(details.getExpenseItems()))
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private String setTotalAmountSum(Double amount) {
        return amount!=null?ExpenseUtils.toINRFormat(amount):ExpenseUtils.DASH;
    }

    private void isExpenseExist(){
        Integer count = expensesRepository.expenseExists(ExpenseUtils.atStartOfDay(),ExpenseUtils.atEndOfDay());
        if (count>0) {
            throw new AlreadyExistsException(ExpenseUtils.EXPENSE_EXISTS);
        }
    }
    private void isExpenseItemExist(Long expenseId,String itemName){
        Integer count = expensesRepository.existsByExpenseName(expenseId,itemName);
        if (count>0) {
            throw new AlreadyExistsException(ExpenseUtils.EXPENSE_ITEM_EXISTS);
        }
    }
    private Set<ExpenseItems> setItems(Set<ExpenseItemDto> dtos,Set<ExpenseItems> expenseItemList) {
        for(ExpenseItemDto dto:dtos) {
            if (expenseItemList.contains(dto.getItemName())) {
                throw new AlreadyExistsException(ExpenseUtils.EXPENSE_ITEM_EXISTS);
            } else {
                expenseItemList.add(ExpenseItems.builder()
                        .itemName(dto.getItemName())
                        .itemPrice(dto.getItemPrice())
                        .paymentType(dto.getPaymentType())
                        .build());
            }
        }
        return expenseItemList;
    }

    private Set<ExpenseItemDto> setItemsDto(Set<ExpenseItems> items){
        return !items.isEmpty()?items.stream()
                .map(data -> ExpenseItemDto.builder()
                        .itemName(data.getItemName())
                        .itemPrice(data.getItemPrice())
                        .paymentType(data.getPaymentType())
                        .build())
                .collect(Collectors.toSet()) : new HashSet<>();
    }
}
