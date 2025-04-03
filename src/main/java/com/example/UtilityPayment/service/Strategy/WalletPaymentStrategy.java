package com.example.UtilityPayment.service.Strategy;

import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.model.User;
import org.springframework.stereotype.Service;

@Service("WALLET")
public class WalletPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(User user, Invoice invoice, double amount) {
        if (user.getWalletBalance() >= amount) {
            user.setWalletBalance(user.getWalletBalance() - amount);
            invoice.setIsPaid("PAID");
            return true;
        }
        return false;
    }
}

