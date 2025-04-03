package com.example.UtilityPayment.service;

import com.example.UtilityPayment.model.*;
import com.example.UtilityPayment.repository.InvoiceRepository;
import com.example.UtilityPayment.repository.PaymentRepository;
import com.example.UtilityPayment.repository.TransactionRepository;
import com.example.UtilityPayment.repository.UserRepository;
import com.example.UtilityPayment.service.Factory.PaymentStrategyFactory;
import com.example.UtilityPayment.service.Strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;

    public boolean processPayment(Long invoiceId, double amount, String paymentMethod,String discountType) {
        System.out.println(amount);
        System.out.println(paymentMethod);
        Invoice invoice = invoiceRepository.findByIdAndIsPaid(invoiceId,"Not Paid")
                .orElseThrow(() -> new RuntimeException("Invoice not found or already paid"));

        User user = userRepository.findById(invoice.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        System.out.println("Not completed");
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);
        if (paymentStrategy == null) {
            throw new RuntimeException("Payment strategy not found for: " + paymentMethod);
        }
        System.out.println(amount);

        boolean paymentSuccess = paymentStrategy.processPayment(user, invoice, amount);

        System.out.println(paymentSuccess);

        if (paymentSuccess) {

            System.out.println(paymentMethod);
            userRepository.save(user);
            invoiceRepository.save(invoice);

            Payment payment = new Payment();
            payment.setInvoiceId(invoiceId);
            payment.setUserId(user.getId());
            payment.setAmountPaid(amount);
            payment.setPaymentMethod(paymentMethod);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);

            Transaction transaction = new Transaction();
            transaction.setInvoice(invoice);
            transaction.setTotalAmount(invoice.getTotalAmount());
            transaction.setDiscountType(discountType);
            transaction.setAmountPaid(amount);
            transaction.setPaymentMethod(PaymentMethod.valueOf(paymentMethod.trim().toUpperCase()));
            transaction.setTransactionStatus("SUCCESS");
            transaction.setTransactionDate(LocalDateTime.now());
            transactionRepository.save(transaction);
            System.out.println("Transaction updated");

            return true;
        } else {
            throw new RuntimeException("Insufficient balance");
        }
    }
}
