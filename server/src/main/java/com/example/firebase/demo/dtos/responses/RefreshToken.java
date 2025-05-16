package com.example.firebase.demo.dtos.responses;

import com.example.firebase.demo.entities.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class RefreshToken {
    private User user;
    private String token;
}