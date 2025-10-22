package com.community.domain.auth.service;

import com.community.domain.auth.model.RefreshToken;
import com.community.domain.auth.repository.RefreshTokenRepository;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenAuthService implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public LoginResult login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        var accessToken = tokenProvider.createAccessToken(Map.of("sub", user.getId()));
        var refreshToken = tokenProvider.createRefreshToken(Map.of("sub", user.getId()));
        refreshTokenRepository.save(new RefreshToken(refreshToken.token(), user.getId(), refreshToken.expiresAt()));

        LoginResponse loginResponse = new LoginResponse(
                accessToken.token(),
                Duration.between(Instant.now(), accessToken.expiresAt()).toSeconds(),
                "Bearer"
        );

        return new LoginResult(loginResponse, refreshToken.token(),
                Duration.between(Instant.now(), refreshToken.expiresAt()).toSeconds());
    }

    public LoginResult refresh(String token) {
        if (token == null || token.isBlank()) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        TokenPayload payload = verifyAndParseRefreshToken(token);

        refreshTokenRepository.delete(token);

        var accessToken = tokenProvider.createAccessToken(Map.of("sub", payload.userId()));
        var refreshToken = tokenProvider.createRefreshToken(Map.of("sub", payload.userId()));
        refreshTokenRepository.save(new RefreshToken(refreshToken.token(), payload.userId(), refreshToken.expiresAt()));

        LoginResponse loginResponse = new LoginResponse(
                accessToken.token(),
                Duration.between(Instant.now(), accessToken.expiresAt()).toSeconds(),
                "Bearer"
        );

        return new LoginResult(loginResponse, refreshToken.token(),
                Duration.between(Instant.now(), refreshToken.expiresAt()).toSeconds());
    }

    public TokenPayload verifyAndParseAccessToken(String token) {

        return tokenProvider.parseAccessToken(token);
    }

    public TokenPayload verifyAndParseRefreshToken(String token) {
        TokenPayload payload = tokenProvider.parseRefreshToken(token);
        RefreshToken stored = refreshTokenRepository.find(token)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH));
        if (!stored.getUserId().equals(payload.userId()) || stored.isExpired()) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        return payload;
    }
}
