package com.example.firebase.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private  String email;

    @Column(nullable = false)
    private  String fullName;

    @Column(nullable = false, length = 1200)
    @ToString.Exclude
    private  String password;

    private String phoneNumber;

    public User(String email, String fullName, String password, String phoneNumber) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}