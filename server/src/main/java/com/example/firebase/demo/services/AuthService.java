package com.example.firebase.demo.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JavaMailSender mailSender;
    private final Random random = new Random();

    // Inject OTP expiration from application.properties
    @Value("${otp.expiration}")
    private long otpExpirationMillis;

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    public void sendOtp(String email) {
        String otp = String.format("%06d", random.nextInt(999999));
        long expiresAt = System.currentTimeMillis() + otpExpirationMillis;

        otpStore.put(email, new OtpEntry(otp, expiresAt));
        sendEmail(email, otp);
    }

    public String verifyOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email);

        if (entry == null || System.currentTimeMillis() > entry.expiresAt) {
            throw new RuntimeException("OTP expired or not found");
        }

        if (!entry.otp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // OTP is valid — remove it
        otpStore.remove(email);

        try {
            FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            try {
                UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest().setEmail(email);
                FirebaseAuth.getInstance().createUser(createRequest);
            } catch (FirebaseAuthException ex) {
                throw new RuntimeException("User creation failed: " + ex.getMessage());
            }
        }

        try {
            return FirebaseAuth.getInstance().createCustomToken(email);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Token generation failed: " + e.getMessage());
        }
    }

    private void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\nThis OTP is valid for " + (otpExpirationMillis / 60000) + " minutes.");
        mailSender.send(message);
    }

    private static class OtpEntry {
        String otp;
        long expiresAt;

        OtpEntry(String otp, long expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }
}
