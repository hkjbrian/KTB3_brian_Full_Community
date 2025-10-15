package com.community.domain.auth.controller;

import com.community.domain.auth.dto.LoginResult;
import com.community.domain.auth.service.AuthService;

import com.community.domain.auth.dto.response.LoginResponse;
import com.community.domain.auth.dto.request.LoginRequest;
import com.community.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest req) {
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
}
