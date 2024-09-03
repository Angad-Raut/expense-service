package com.projectx.expense_service.payloads;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {
    private Long id;
    private String expenseDate;
    @NotEmpty(message = "Please provide at least one item!!")
    private Set<ExpenseItemDto> expenseItemDtos=new HashSet<>();
}
