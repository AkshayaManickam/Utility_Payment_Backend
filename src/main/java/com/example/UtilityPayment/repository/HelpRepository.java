package com.example.UtilityPayment.repository;


import com.example.UtilityPayment.model.Help;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HelpRepository extends JpaRepository<Help, Long> {
    List<Help> findByUserMail(String userMail);
}
