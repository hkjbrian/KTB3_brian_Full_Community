package com.community.domain.auth.controller;

import com.community.domain.auth.dto.request.LoginRequest;
import com.community.domain.auth.dto.response.LoginResponse;
import com.community.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증/인가 관련 API")
public interface AuthApiSpec {

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인을 요청합니다.")
    ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest req);

    @Operation(summary = "토큰 재발급", description = "accessToken 이 만료되었을 경우, 쿠키의 refreshToken 을 통해 토큰을 재발급합니다.")
    ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    );
}


