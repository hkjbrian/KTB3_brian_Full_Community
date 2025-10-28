package com.community.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "로그인 응답 DTO")
@Getter
@AllArgsConstructor
public class LoginResponse {

    private final String accessToken;
    private final long expiresIn;
    private final String type;
}
