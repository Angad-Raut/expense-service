package com.projectx.expense_service.repository;

import com.projectx.expense_service.entity.ExpensesDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ExpensesRepository extends JpaRepository<ExpensesDetails,Long> {

    @Query(value = "select * from expenses_details where id=:expenseId",nativeQuery = true)
    ExpensesDetails getExpensesById(@Param("expenseId")Long expenseId);

    @Query(value = "select count(*) from expenses_details expense "
            +"join expenses_items item on expense.id=item.expenses_id "
            +"where expense.id=:expenseId and item.item_name=:itemName",nativeQuery = true)
    Integer existsByExpenseName(@Param("expenseId")Long expenseId,@Param("itemName")String itemName);

    @Query(value = "select count(*) from expenses_details "
            +"where inserted_time between :startDate and :endDate",nativeQuery = true)
    Integer expenseExists(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value = "select count(*) from expenses_details",nativeQuery = true)
    Integer getAllExpenseCount();

    @Modifying
    @Transactional
    @Query(value = "update expenses_details set status=:status where id=:expenseId",nativeQuery = true)
    Integer updateStatus(@Param("expenseId")Long expenseId,@Param("status")Boolean status);

    @Query(value = "select * from expenses_details expense "
            +"where inserted_time between :startDate and :endDate",nativeQuery = true)
    List<ExpensesDetails> getAllExpensesWithDates(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value = "select * from expenses_details expense "
            +"where inserted_time between :startDate and :endDate",nativeQuery = true)
    Page<ExpensesDetails> getAllExpensesPagesWithDateRange(@Param("startDate")Date startDate, @Param("endDate")Date endDate, Pageable pageable);

    @Query(value = "select * from expenses_details expense ",nativeQuery = true)
    List<ExpensesDetails> getAllExpenses();

    @Query(value = "select * from expenses_details",nativeQuery = true)
    Page<ExpensesDetails> getAllExpensesPages(Pageable pageable);

    @Query(value = "select it.item_name,it.item_price,it.payment_type from expenses_details expense "
            +"join expenses_items it on expense.id=it.expenses_id "
            +"where expense.id=:expenseId",nativeQuery = true)
    List<Object[]> getExpenseItemsByExpenseId(@Param("expenseId")Long expenseId);

    @Query(value = "select sum(exp.total_amount) from expenses_details exp "
            +"where exp.inserted_time between :startDate and :endDate",nativeQuery = true)
    Double getTotalAmountSum(@Param("startDate")Date startDate,@Param("endDate")Date endDate);
}
