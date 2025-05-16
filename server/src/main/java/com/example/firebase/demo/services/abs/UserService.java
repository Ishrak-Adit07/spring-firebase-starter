package com.example.firebase.demo.services.abs;

import com.example.firebase.demo.entities.User;

public interface UserService {

    User saveUser(User user);
    User getUserById(Long userId);
    void updateUserInfo(Long userId, String email, String phoneNumber);
}
