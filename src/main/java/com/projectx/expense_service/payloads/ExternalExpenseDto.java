package com.projectx.expense_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalExpenseDto {
    private Long id;
    private String personName;
    private String description;
    private Double amount;
    private String amountGivenDate;
}
