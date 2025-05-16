package com.example.firebase.demo.controllers;

import com.example.firebase.demo.dtos.requests.OtpVerificationRequest;
import com.example.firebase.demo.dtos.requests.RegistrationRequest;
import com.example.firebase.demo.repositories.UserRepository;
import com.example.firebase.demo.services.abs.AuthService;
import com.example.firebase.demo.services.abs.UserService;
import com.example.firebase.demo.services.abs.UserVerificationService;
import com.example.firebase.demo.utils.CodeGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserVerificationService userVerificationService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test mail received");
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest registrationRequest){
        userService.registerUser(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestBody @Valid OtpVerificationRequest request) {
        String token = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(Map.of("accessToken", token));
    }
}

