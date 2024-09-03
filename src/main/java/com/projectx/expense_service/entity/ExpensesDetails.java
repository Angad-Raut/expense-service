package com.projectx.expense_service.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "expenses_details")
public class ExpensesDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "status")
    private Boolean status;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "expenses_items", joinColumns = @JoinColumn(name = "expenses_id"))
    private Set<ExpenseItems> expenseItems=new HashSet<>();
    @Column(name = "inserted_time")
    private Date insertedTime;
}
