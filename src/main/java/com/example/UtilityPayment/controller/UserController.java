package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/wallet")
    public ResponseEntity<Double> getWalletBalance(@RequestParam String email) {
        double balance = userService.getWalletBalanceByEmail(email);
        return ResponseEntity.ok(balance);
    }
}

