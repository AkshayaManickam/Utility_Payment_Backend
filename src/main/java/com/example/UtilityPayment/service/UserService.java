package com.example.UtilityPayment.service;

import com.example.UtilityPayment.model.User;
import com.example.UtilityPayment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public double getWalletBalanceByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getWalletBalance)
                .orElse(0.0); // Default to 0 if user not found
    }
}

