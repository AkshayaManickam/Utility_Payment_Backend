package com.example.UtilityPayment.service;

import com.example.UtilityPayment.model.AuthenticationDetails;
import com.example.UtilityPayment.model.User;
import com.example.UtilityPayment.repository.AuthenticationRepository;
import com.example.UtilityPayment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    private final Random random = new Random();

    public String generateOtp(String email) {
        Optional<User> user= userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "Email does not exist!";
        }

        String otp = String.valueOf(100000 + random.nextInt(900000)); // 6-digit OTP
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setEmail(email);
        authDetails.setOtp(otp);
        authDetails.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

        authenticationRepository.save(authDetails);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<AuthenticationDetails> authDetails = authenticationRepository.findByEmailAndOtp(email, otp);
        return authDetails.isPresent() && authDetails.get().getExpiresAt().isAfter(LocalDateTime.now());
    }
}
