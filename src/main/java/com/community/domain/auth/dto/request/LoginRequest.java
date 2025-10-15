package com.community.domain.auth.dto.request;

import com.community.global.validation.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "이메일 값은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호 값은 필수입니다.")
    @Password
    @Size(min = 8, max = 20 , message = "비밀번호는 8자 이상, 20자 이하입니다.")
    private String password;
}
