package com.example.UtilityPayment.repository;


import com.example.UtilityPayment.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
    // Custom query to fetch bills with "Not Paid" status for a given user
    List<Invoice> findPendingBillsByUserEmail(String userEmail);
}
