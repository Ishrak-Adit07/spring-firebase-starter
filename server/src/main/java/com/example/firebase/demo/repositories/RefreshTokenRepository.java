package com.example.firebase.demo.repositories;

import com.example.firebase.demo.dtos.responses.RefreshToken;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "none")
public class RefreshTokenRepository {
    private Map<String, RefreshTokenWrapper> tokenMap;
    private Map<Long, String> userTokenMap;

    @Value("${auth.jwt.refresh-token.timeout}")
    private int refreshTokenTimeout;

    @PostConstruct
    public void init() {
        tokenMap = new HashMap<>();
        userTokenMap = new HashMap<>();
    }

    public void saveToken(RefreshToken token) {
        Long userId = token.getUser().getId();
        deleteTokenForUserId(userId);
        tokenMap.put(token.getToken(), new RefreshTokenWrapper(token, Instant.now().plusSeconds(refreshTokenTimeout)));
        userTokenMap.put(userId, token.getToken());
    }

    public Optional<RefreshToken> findByToken(String hashedToken) {
        if (tokenMap.containsKey(hashedToken)) {
            RefreshTokenWrapper refreshTokenWrapper = tokenMap.get(hashedToken);
            if (refreshTokenWrapper.expiryTime().isBefore(Instant.now())) {
                tokenMap.remove(hashedToken);
                return Optional.empty();
            }
            return Optional.of(refreshTokenWrapper.refreshToken());
        }
        return Optional.empty();
    }

    public void deleteTokenForUserId(Long userId) {
        if (userTokenMap.containsKey(userId)) {
            tokenMap.remove(userTokenMap.get(userId));
            userTokenMap.remove(userId);
        }
    }

    record RefreshTokenWrapper(RefreshToken refreshToken, Instant expiryTime) {
    }
}