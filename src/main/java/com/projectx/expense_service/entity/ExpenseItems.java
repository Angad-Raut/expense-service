package com.projectx.expense_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ExpenseItems {
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_price")
    private Double itemPrice;
    @Column(name = "payment_type")
    private String paymentType;
}
