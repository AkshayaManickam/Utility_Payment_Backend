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
import java.util.*;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;  // UserRepository instead of EmployeeRepository

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    private final Random random = new Random();

    public Map<String, String> generateOtp(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        Map<String, String> response = new HashMap<>();

        if (user.isEmpty()) {
            response.put("message", "Email does not exist!");
            return response; // Return response with error message
        }

        boolean hadActiveSession = expireExistingSessions(email);

        String otp = String.valueOf(100000 + random.nextInt(900000)); // Generate 6-digit OTP

        // Save OTP to the database with expiration time
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setEmail(email);
        authDetails.setOtp(otp);
        authDetails.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

        authenticationRepository.save(authDetails);

        if (hadActiveSession) {
            response.put("message", "Previous session cleared. New OTP generated.");
        } else {
            response.put("message", "OTP generated successfully.");
        }
        response.put("otp", otp);

        return response;
    }

    private boolean expireExistingSessions(String email) {
        boolean hadActiveSession = false;
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            if (principal instanceof String) {
                String loggedInEmail = (String) principal;
                if (loggedInEmail.equalsIgnoreCase(email)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    if (sessions != null) {
                        for (SessionInformation sessionInfo : sessions) {
                            sessionInfo.expireNow(); // Expire the old session
                            hadActiveSession = true;
                        }
                    }
                }
            }
        }
        return hadActiveSession;
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
