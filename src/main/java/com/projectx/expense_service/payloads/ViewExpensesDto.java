package com.projectx.expense_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewExpensesDto {
    private Integer srNo;
    private Long expenseId;
    private String totalAmount;
    private String expenseDate;
}
