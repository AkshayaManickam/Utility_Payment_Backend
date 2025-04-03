package com.example.UtilityPayment.repository;

import com.example.UtilityPayment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceId(Long invoiceId);

    List<Payment> findByUserId(Long userId);

    List<Payment> findTop3ByUserIdAndPaymentMethodOrderByPaymentDateDesc(Long userId, String paymentMethod);
}
