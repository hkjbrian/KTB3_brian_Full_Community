package com.community.domain.auth.service;

import com.community.domain.auth.dto.TokenPayload;
import com.community.domain.auth.dto.TokenResult;

import java.util.Map;

public interface TokenProvider {

    TokenPayload parseAccessToken(String accessToken);

    TokenPayload parseRefreshToken(String refreshToken);

    TokenResult createAccessToken(Map<String, Object> claims);

    TokenResult createRefreshToken(Map<String, Object> claims);
}
