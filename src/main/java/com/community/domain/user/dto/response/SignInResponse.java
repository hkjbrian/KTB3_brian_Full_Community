package com.community.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "회원가입 응답 DTO")
@Data
@AllArgsConstructor
public class SignInResponse {

    public Long id;
}
