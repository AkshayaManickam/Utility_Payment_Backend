package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.User;
import com.example.UtilityPayment.repository.UserRepository;
import com.example.UtilityPayment.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4300")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private SessionRegistry sessionRegistry;

    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = authenticationService.generateOtp(email);
        if (response.get("message").equals("Email does not exist!")) {
            return ResponseEntity.badRequest().body(response);
        }
        if (response.get("message").equals("User already logged in. Cannot generate OTP.")) {
            return ResponseEntity.status(409).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request,
                                                         HttpSession session) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = authenticationService.verifyOtp(email, otp);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            userOpt.ifPresent(user -> {
                response.put("userId", user.getId());
                response.put("sessionId", session.getId());
                session.setAttribute("email", email);  // Store email in the session

                // Create Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );

                // Create SecurityContext
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);

                session.setAttribute("SPRING_SECURITY_CONTEXT", context);  // Store the context in session

                sessionRegistry.registerNewSession(session.getId(), email);

            });
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        String sessionId = session.getId();
        // Invalidate the session (removes the session context)
        session.invalidate();

        // Expire the session from the session registry
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            sessions.forEach(si -> {
                if (si.getSessionId().equals(sessionId)) {
                    si.expireNow(); // Mark session as expired
                }
            });
        });

        // Return a JSON response instead of a plain string
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session-status")
    public ResponseEntity<Map<String, Object>> sessionStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String email = (String) session.getAttribute("email");

        if (email != null) {
            response.put("isLoggedIn", true);
            response.put("email", email);
        } else {
            response.put("isLoggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}
