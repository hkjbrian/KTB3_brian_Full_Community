package com.community.domain.auth.dto;

import com.community.domain.auth.dto.response.LoginResponse;

public record LoginResult(
        LoginResponse tokenResponse,
        String refreshToken,
        long refreshTokenExpiresInSeconds
){}
