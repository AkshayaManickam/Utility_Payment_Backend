package com.example.UtilityPayment.service.Strategy;

import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.model.User;

public interface PaymentStrategy {
    boolean processPayment(User user, Invoice invoice, double amount);
}
