package com.community.domain.auth.service;

import com.community.domain.auth.JwtTokenProvider;
import com.community.domain.auth.RefreshTokenStore;
import com.community.domain.auth.dto.TokenPayload;
import com.community.domain.auth.dto.LoginResult;
import com.community.domain.user.model.User;
import com.community.domain.user.repository.UserRepository;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import com.community.domain.auth.dto.response.LoginResponse;
import com.community.domain.auth.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenStore refreshTokenStore;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResult login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        var accessToken = jwtTokenProvider.createAccessToken(user.getId());
        var refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        refreshTokenStore.store(refreshToken.token(), user.getId(), refreshToken.expiresAt());

        LoginResponse loginResponse = new LoginResponse(
                accessToken.token(),
                Duration.between(Instant.now(), accessToken.expiresAt()).toSeconds(),
                "Bearer"
        );

        return new LoginResult(loginResponse, refreshToken.token(),
                Duration.between(Instant.now(), refreshToken.expiresAt()).toSeconds());
    }

    public TokenPayload verifyAndParseAccessToken(String token) {

        return jwtTokenProvider.parseAccessToken(token);
    }

    public TokenPayload verifyAndParseRefreshToken(String token) {
        TokenPayload payload = jwtTokenProvider.parseRefreshToken(token);
        RefreshTokenStore.RefreshToken stored = refreshTokenStore.find(token)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH));
        if (!stored.userId().equals(payload.userId()) || stored.isExpired()) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        return payload;
    }

    public LoginResult refresh(String token) {
        TokenPayload payload = verifyAndParseRefreshToken(token);

        refreshTokenStore.delete(token);

        var accessToken = jwtTokenProvider.createAccessToken(payload.userId());
        var refreshToken = jwtTokenProvider.createRefreshToken(payload.userId());
        refreshTokenStore.store(refreshToken.token(), payload.userId(), refreshToken.expiresAt());

        LoginResponse loginResponse = new LoginResponse(
                accessToken.token(),
                Duration.between(Instant.now(), accessToken.expiresAt()).toSeconds(),
                "Bearer"
        );

        return new LoginResult(loginResponse, refreshToken.token(),
                Duration.between(Instant.now(), refreshToken.expiresAt()).toSeconds());
    }
}
