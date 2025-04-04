package com.example.UtilityPayment.repository;

import com.example.UtilityPayment.model.AuthenticationDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<AuthenticationDetails, Long> {
    Optional<AuthenticationDetails> findByEmailAndOtp(String email, String otp);

}
