package com.projectx.expense_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewReportDto {
    private Integer srNo;
    private Long expenseId;
    private String totalAmount;
    private Double total;
    private String expenseDate;
    private Integer itemCount;
    List<ViewExpenseItemsDto> itemsList = new ArrayList<>();
}
