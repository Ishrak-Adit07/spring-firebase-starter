package com.example.firebase.demo.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
