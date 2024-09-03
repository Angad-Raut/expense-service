package com.projectx.expense_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRangePageRequestDto {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortParam;
    private String sortDir;
    private String startDate;
    private String endDate;
}
