package com.example.firebase.demo.controllers;

import com.example.firebase.demo.dtos.OtpRequest;
import com.example.firebase.demo.dtos.OtpVerificationRequest;
import com.example.firebase.demo.services.abs.AuthService;
import com.example.firebase.demo.services.abs.UserVerificationService;
import com.example.firebase.demo.utils.CodeGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserVerificationService userVerificationService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test mail received");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid OtpRequest request) {
        String otp = CodeGenerator.generateOtp();
        userVerificationService.sendVerificationEmail(request.getEmail(), otp);
        return ResponseEntity.ok("OTP sent to " + request.getEmail());
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestBody @Valid OtpVerificationRequest request) {
        String token = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(Map.of("accessToken", token));
    }
}

