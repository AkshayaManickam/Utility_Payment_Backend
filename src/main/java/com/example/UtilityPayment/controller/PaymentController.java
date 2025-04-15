package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.Payment;
import com.example.UtilityPayment.model.User;
import com.example.UtilityPayment.repository.PaymentRepository;
import com.example.UtilityPayment.repository.UserRepository;
import com.example.UtilityPayment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> payInvoice(@RequestBody Map<String, Object> requestData) {
        System.out.println("Received Payment Request: " + requestData);

        try {
            Long invoiceId = ((Number) requestData.get("invoiceId")).longValue();
            double amountPaid = ((Number) requestData.get("amount")).doubleValue();
            String paymentMethod = (String) requestData.get("paymentMethod");
            String discountType = (String) requestData.get("discountType");

            boolean success = paymentService.processPayment(invoiceId, amountPaid, paymentMethod, discountType);

            System.out.println(success);
            Map<String, String> response = new HashMap<>();
            if (success) {
                response.put("message", "Payment successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Payment failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @GetMapping("/user")
    public List<Payment> getPaymentsByUserEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            return paymentRepository.findByUserId(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/wallet-transactions")
    public ResponseEntity<List<Map<String, Object>>> getWalletTransactions(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Payment> walletPayments = paymentRepository.findTop3ByUserIdAndPaymentMethodOrderByPaymentDateDesc(user.getId(), "WALLET");

        List<Map<String, Object>> transactions = walletPayments.stream().map(payment -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", payment.getId());
            map.put("amount", payment.getAmountPaid());
            map.put("paymentDate", payment.getPaymentDate());
            map.put("paymentMethod", payment.getPaymentMethod());
            return map;
        }).toList();

        return ResponseEntity.ok(transactions);
    }


}

