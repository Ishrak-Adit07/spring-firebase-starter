package com.example.firebase.demo.dtos.requests;

import com.example.firebase.demo.entities.User;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UnverifiedUser implements Serializable {
    private User user;
    private String otp;
}