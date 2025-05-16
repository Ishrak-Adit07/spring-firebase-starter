package com.example.firebase.demo.services.abs;

public interface AuthService {

    public void sendOtp(String email);
    public String verifyOtp(String email, String otp);
}
