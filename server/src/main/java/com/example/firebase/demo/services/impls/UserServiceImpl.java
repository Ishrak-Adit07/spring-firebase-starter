package com.example.firebase.demo.services.impls;

import com.example.firebase.demo.dtos.requests.RegistrationRequest;
import com.example.firebase.demo.entities.User;
import com.example.firebase.demo.repositories.UserRepository;
import com.example.firebase.demo.repositories.UserVerificationRepository;
import com.example.firebase.demo.services.abs.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;

    @Override
    public User saveUser(User user) {
        userVerificationRepository.deleteUserVerInfoByEmail(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }

    @Override
    public void updateUserInfo(Long userId, String email, String phoneNumber) {
        User user = getUserById(userId);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        userRepository.save(new User(registrationRequest.email(), registrationRequest.fullName(), registrationRequest.password(), registrationRequest.phoneNumber()));
    }
}