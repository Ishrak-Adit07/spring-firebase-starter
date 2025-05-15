package com.example.firebase.demo.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    // Use in-memory store, or Redis/DB
    private final Map<String, String> otpStore = new ConcurrentHashMap<>() {
    };
    private final Random random = new Random();

    public void sendOtp(String email) {
        String otp = String.format("%06d", random.nextInt(999999));

        // Save OTP
        otpStore.put(email, otp);

        // Send via email (replace this with real email service)
        System.out.println("OTP for " + email + ": " + otp);

        // You could integrate Firebase Functions + SendGrid or SMTP here
    }

    public String verifyOtp(String email, String otp) {
        if (!otp.equals(otpStore.get(email))) {
            throw new RuntimeException("Invalid OTP");
        }

        // OTP is correct, now issue a custom Firebase token (optional)
        try {
//            UserRecord user = FirebaseAuth.getInstance().getUserByEmail(email);
            FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            // If not exists, create
            try {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(email);
                FirebaseAuth.getInstance().createUser(request);
            } catch (FirebaseAuthException ex) {
                throw new RuntimeException("User creation failed");
            }
        }

        try {
            String customToken = FirebaseAuth.getInstance().createCustomToken(email);
            otpStore.remove(email); // Invalidate OTP
            return customToken;
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Token generation failed");
        }
    }
}

