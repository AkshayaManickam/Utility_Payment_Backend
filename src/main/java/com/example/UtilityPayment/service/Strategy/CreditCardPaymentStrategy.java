package com.example.UtilityPayment.service.Strategy;

import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.model.User;
import org.springframework.stereotype.Service;

@Service("CREDIT")
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(User user, Invoice invoice, double amount) {
        if (user.getCreditCardBalance() >= amount) {
            user.setCreditCardBalance(user.getCreditCardBalance() - amount);
            invoice.setIsPaid("PAID");
            return true;
        }
        return false;
    }
}

