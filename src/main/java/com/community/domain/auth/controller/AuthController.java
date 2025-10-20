package com.community.domain.auth.controller;

import com.community.domain.auth.dto.LoginResult;
import com.community.domain.auth.service.AuthService;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import com.community.domain.auth.dto.response.LoginResponse;
import com.community.domain.auth.dto.request.LoginRequest;
import com.community.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증/인가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인을 요청합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest req) {
        LoginResult result = authService.login(req);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(result.refreshTokenExpiresInSeconds())
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("로그인에 성공하였습니다.", result.tokenResponse()));
    }

    @Operation(summary = "토큰 재발급", description = "accessToken 이 만료되었을 경우, 쿠키의 refreshToken 을 통해 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        LoginResult result = authService.refresh(refreshToken);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(result.refreshTokenExpiresInSeconds())
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("토큰이 갱신되었습니다.", result.tokenResponse()));
    }
}
