package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.User;
import com.example.UtilityPayment.repository.UserRepository;
import com.example.UtilityPayment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/wallet")
    public ResponseEntity<Double> getWalletBalance(@RequestParam String email) {
        double balance = userService.getWalletBalanceByEmail(email);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<Integer> getUserConsumption(@PathVariable("userEmail") String userEmail) {
        Integer units = userService.getUnitsConsumedByUser(userEmail);
        if (units == null) {
            return ResponseEntity.notFound().build();  // Return 404 properly
        }
        return ResponseEntity.ok(units);
    }
}

