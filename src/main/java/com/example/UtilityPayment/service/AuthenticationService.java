package com.example.UtilityPayment.service;

import com.example.UtilityPayment.model.AuthenticationDetails;
import com.example.UtilityPayment.model.User;  // Assuming you have a User model instead of Employee
import com.example.UtilityPayment.repository.AuthenticationRepository;
import com.example.UtilityPayment.repository.UserRepository;  // Assuming you have a UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;  // UserRepository instead of EmployeeRepository

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    private final Random random = new Random();

    public String generateOtp(String email) {
        Optional<User> user = userRepository.findByEmail(email);  // Using User model instead of Employee

        if (user.isEmpty()) {
            return "Email does not exist!";
        }

        if (hasActiveSession(email)) {
            return "User already logged in. Cannot generate OTP.";
        }

        String otp = String.valueOf(100000 + random.nextInt(900000));

        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setEmail(email);
        authDetails.setOtp(otp);
        authDetails.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

        authenticationRepository.save(authDetails);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<AuthenticationDetails> authDetails = authenticationRepository.findByEmailAndOtp(email, otp);

        if (authDetails.isEmpty()) {
            return false;
        }

        if (authDetails.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public boolean hasActiveSession(String email) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            if (principal instanceof String) {
                String loggedInEmail = (String) principal;
                if (loggedInEmail.equalsIgnoreCase(email)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    if (sessions != null && !sessions.isEmpty()) {
                        return true;  // Active session found for the user
                    }
                }
            }
        }
        return false;  // No active session found for the user
    }
}
