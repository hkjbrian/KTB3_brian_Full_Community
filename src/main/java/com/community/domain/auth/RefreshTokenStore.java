package com.community.domain.auth;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshTokenStore {

    private final Map<String, RefreshToken> store = new ConcurrentHashMap<>();

    public void store(String token, Long userId, Instant expiresAt) {
        store.put(token, new RefreshToken(userId, expiresAt));
    }

    public Optional<RefreshToken> find(String token) {
        return Optional.ofNullable(store.get(token));
    }

    public void delete(String token) {
        store.remove(token);
    }

    public record RefreshToken(Long userId, Instant expiresAt) {
        public boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
