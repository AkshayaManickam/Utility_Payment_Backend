package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.Help;
import com.example.UtilityPayment.model.Status;
import com.example.UtilityPayment.repository.AuthenticationRepository;
import com.example.UtilityPayment.repository.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help")
@CrossOrigin(origins = "http://localhost:4300") // Allow frontend access
public class HelpController {

    @Autowired
    private HelpRepository helpRepository;

    @PostMapping("/create")
    public Help createHelpRequest(@RequestBody Help help) {
        help.setStatus(Status.SENT); // Default status
        return helpRepository.save(help);
    }


    @GetMapping("/user/{userMail}")
    public List<Help> getHelpRequestsByUser(@PathVariable String userMail) {
        return helpRepository.findByUserMail(userMail);
    }

}
