package com.example.firebase.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserVerificationService {
    @Value("${verification.email.message}")
    private String verificationEmailMessage;

    @Value("${verification.email.subject}")
    private String verificationEmailSubject;

    @Value("${verification.email.timeout}")
    private int optExpiration;

//    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
//    private final UserVerificationRepository userVerRepo;

//    public void cacheDetails(RegistrationRequest request, String otp) throws JsonProcessingException {
//        UnverifiedUser unverifiedUser = new UnverifiedUser(
//                new User(request.email(),
//                        request.fullName(),
//                        passwordEncoder.encode(request.password()),
//                        request.role(),
//                        request.phoneNumber())
//                , otp);
//
//        userVerRepo.putUserVerificationInfo(request.email(), unverifiedUser);
//    }

    public void sendVerificationEmail(String email, String otp) {
        emailService.sendSimpleEmail(email, verificationEmailSubject,
                String.format(verificationEmailMessage, otp, (optExpiration / 60)));
    }

//    public User verifyUser(String email, String otp) throws JsonProcessingException {
//        Optional<UnverifiedUser> unverifiedUser = userVerRepo.getUserVerificationInfoByEmail(email);
//
//        if (unverifiedUser.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Your OTP has expired. Please try again");
//        }
//
//        if(!unverifiedUser.get().getOtp().equals(otp)){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your OTP doesn't match. Please try again");
//        }
//        return unverifiedUser.get().getUser();
//    }

}
