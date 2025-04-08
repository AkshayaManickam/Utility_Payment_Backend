package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.Employee;
import com.example.UtilityPayment.model.Help;
import com.example.UtilityPayment.model.Status;
import com.example.UtilityPayment.model.User;
import com.example.UtilityPayment.repository.EmployeeRepository;
import com.example.UtilityPayment.repository.HelpRepository;
import com.example.UtilityPayment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/create")
    public Help createHelpRequest(@RequestBody Help help) {
        help.setStatus(Status.SENT);
        Optional<User> userOptional = userRepository.findByEmail(help.getUserMail());
        if (userOptional.isPresent()) {
            String userAddress = userOptional.get().getAddress();
            String[] addressParts = userAddress.split(",");
            if (addressParts.length > 1) {
                String location = addressParts[1].trim();
                Optional<Employee> employeeOptional = employeeRepository.findByLocation(location);
                if (employeeOptional.isPresent()) {
                    Employee assignedEmployee = employeeOptional.get();
                    help.setAssignedTo(assignedEmployee.getEmployeeId());
                    System.out.println("Assigned to Employee: " + assignedEmployee.getName());
                } else {
                    System.out.println("No employee found for location: " + location);
                }
            } else {
                System.out.println("Invalid address format for user: " + userAddress);
            }
        } else {
            System.out.println("User not found for email: " + help.getUserMail());
        }
        return helpRepository.save(help);
    }

    @GetMapping("/user/{userMail}")
    public List<Help> getHelpRequestsByUser(@PathVariable String userMail) {
        return helpRepository.findByUserMail(userMail);
    }

}
