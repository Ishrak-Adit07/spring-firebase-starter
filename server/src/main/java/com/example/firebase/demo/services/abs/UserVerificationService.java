package com.example.firebase.demo.services.abs;

public interface UserVerificationService {

    void sendVerificationEmail(String email, String otp);

}
