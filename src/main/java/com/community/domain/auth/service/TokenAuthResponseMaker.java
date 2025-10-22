package com.community.domain.auth.service;

import com.community.domain.auth.dto.LoginResult;
import com.community.domain.auth.dto.response.LoginResponse;
import com.community.global.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthResponseMaker implements AuthResponseMaker {

    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> makeLoginResponse(LoginResult result) {
        ResponseCookie refreshCookie = makeResponseCookie(result);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("로그인에 성공하였습니다.", result.tokenResponse()));
    }

    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> makeRefreshResponse(LoginResult result) {
        ResponseCookie refreshCookie = makeResponseCookie(result);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("토큰이 갱신되었습니다.", result.tokenResponse()));
    }

    private ResponseCookie makeResponseCookie(LoginResult result) {
        return ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(result.refreshTokenExpiresInSeconds())
                .sameSite("Lax")
                .build();
    }
}
