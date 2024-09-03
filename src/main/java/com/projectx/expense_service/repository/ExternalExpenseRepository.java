package com.projectx.expense_service.repository;

import com.projectx.expense_service.entity.ExternalExpenseDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalExpenseRepository extends JpaRepository<ExternalExpenseDetails,Long> {
    @Query(value = "select * from external_expense_details where id=:id",nativeQuery = true)
    ExternalExpenseDetails getById(@Param("id")Long id);

    @Transactional
    @Modifying
    @Query(value = "update external_expense_details set status=:status where id=:id",nativeQuery = true)
    Integer updateStatus(@Param("id")Long id,@Param("status")Boolean status);

    @Query(value = "select * from external_expense_details where status=:status",nativeQuery = true)
    Page<ExternalExpenseDetails> getAllExternalExpenses(@Param("status")Boolean status, Pageable pageable);
}
