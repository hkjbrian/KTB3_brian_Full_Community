package com.community.domain.auth.repository;

import com.community.domain.auth.model.RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<String, RefreshToken> store = new ConcurrentHashMap<>();

    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.getToken(), refreshToken);
    }

    public Optional<RefreshToken> find(String token) {
        return Optional.ofNullable(store.get(token));
    }

    public void delete(String token) {
        store.remove(token);
    }
}
