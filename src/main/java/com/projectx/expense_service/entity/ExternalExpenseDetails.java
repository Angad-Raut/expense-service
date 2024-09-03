package com.projectx.expense_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "external_expense_details")
public class ExternalExpenseDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "person_name")
    private String personName;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "amount_given_date")
    private Date amountGivenDate;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "inserted_date")
    private Date insertedDate;
}
