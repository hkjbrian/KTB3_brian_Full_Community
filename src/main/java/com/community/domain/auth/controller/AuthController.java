package com.community.domain.auth.controller;

import com.community.domain.auth.dto.LoginResult;
import com.community.domain.auth.service.AuthResponseMaker;
import com.community.domain.auth.service.AuthService;
import com.community.domain.auth.service.TokenAuthService;
import com.community.domain.auth.dto.response.LoginResponse;
import com.community.domain.auth.dto.request.LoginRequest;
import com.community.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final AuthResponseMaker authResponseMaker;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인을 요청합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest req) {
        LoginResult result = authService.login(req);

        return authResponseMaker.makeLoginResponse(result);
    }

    @Operation(summary = "토큰 재발급", description = "accessToken 이 만료되었을 경우, 쿠키의 refreshToken 을 통해 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        LoginResult result = authService.refresh(refreshToken);

        return authResponseMaker.makeRefreshResponse(result);
    }
}
