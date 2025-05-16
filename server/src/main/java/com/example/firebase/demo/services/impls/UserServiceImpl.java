package com.example.firebase.demo.services.impls;

import com.example.firebase.demo.entities.User;
import com.example.firebase.demo.repositories.UserRepository;
import com.example.firebase.demo.repositories.UserVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;

    public User saveUser(User user) {
        userVerificationRepository.deleteUserVerInfoByEmail(user.getEmail());
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }

    public void updateUserInfo(Long userId, String email, String phoneNumber) {
        User user = getUserById(userId);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }
}