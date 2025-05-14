package com.example.firebase.demo.services;

import com.example.firebase.demo.entities.User;
import com.example.firebase.demo.repositories.UserRepository;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void sendEmailLink(String email) throws FirebaseAuthException {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                .setUrl("http://localhost:8080/auth/callback")
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.example.app")
                .build();

        String link = FirebaseAuth.getInstance().generateSignInWithEmailLink(email, actionCodeSettings);

        // Simulate sending email (replace with real email service)
        System.out.println("EMAIL SIGN-IN LINK: " + link);
    }

    public User verifyEmailLink(String email, String oobCode) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(oobCode);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setVerified(true);
            newUser.setFirebaseUid(decodedToken.getUid());
            return userRepository.save(newUser);
        });

        if (!user.isVerified()) {
            user.setVerified(true);
            userRepository.save(user);
        }

        return user;
    }
}
