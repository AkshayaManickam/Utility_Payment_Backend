package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.*;
import com.example.UtilityPayment.repository.EmployeeRepository;
import com.example.UtilityPayment.repository.HelpRepository;
import com.example.UtilityPayment.repository.InvoiceRepository;
import com.example.UtilityPayment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/help")
@CrossOrigin(origins = "http://localhost:4300") // Allow frontend access
public class HelpController {

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createHelpRequest(@RequestBody Help help) {
        help.setStatus(Status.SENT);
        Optional<User> userOptional = userRepository.findByEmail(help.getUserMail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found with email: " + help.getUserMail());
        }
        Help savedHelp = helpRepository.save(help);
        return ResponseEntity.ok(savedHelp);
    }

    @GetMapping("/user/{userMail}")
    public List<Help> getHelpRequestsByUser(@PathVariable String userMail) {
        return helpRepository.findByUserMail(userMail);
    }

}
