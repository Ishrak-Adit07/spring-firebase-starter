package com.example.firebase.demo.services.abs;

public interface AuthService {

    void sendOtp(String email);
    String verifyOtp(String email, String otp);
}
