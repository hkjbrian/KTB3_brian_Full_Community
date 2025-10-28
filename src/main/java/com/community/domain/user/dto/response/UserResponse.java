package com.community.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "회원 정보 응답 DTO")
@Getter
@AllArgsConstructor
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String imageUrl;
}
