package com.example.UtilityPayment.service.Strategy;

import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.model.User;
import org.springframework.stereotype.Service;

@Service("DEBIT")
public class DebitCardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(User user, Invoice invoice, double amount) {
        if (user.getDebitCardBalance() >= amount) {
            user.setDebitCardBalance(user.getDebitCardBalance() - amount);
            invoice.setIsPaid("PAID");
            return true;
        }
        return false;
    }
}

