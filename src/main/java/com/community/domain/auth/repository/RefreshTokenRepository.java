package com.community.domain.auth.repository;

import com.community.domain.auth.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    Optional<RefreshToken> find(String token);

    void delete(String token);
}
