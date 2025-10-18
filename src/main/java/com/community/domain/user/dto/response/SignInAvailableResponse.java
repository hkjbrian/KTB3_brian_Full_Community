package com.community.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "회원가입 정보 검증 응답 DTO")
@Data
@AllArgsConstructor
public class SignInAvailableResponse {

    @Schema(description = "회원 가입 정보 사용 가능 여부", example = "true")
    Boolean isSignInInformationAvailable;
}
