package com.example.UtilityPayment.repository;



import com.example.UtilityPayment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByInvoiceIdIn(List<Long> invoiceIds);
}