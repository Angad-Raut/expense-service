package com.projectx.expense_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseInfoDto {
    private Long id;
    private Double totalAmount;
    private Boolean status;
    private Date insertedTime;
    private Set<ExpenseItemDto> expenseItems=new HashSet<>();
}
