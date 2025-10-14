package com.community.domain.user.dto.request;

import com.community.global.validation.annotation.NoWhiteSpace;
import com.community.global.validation.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignInRequest {

    @NotBlank(message = "이메일 값은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호 값은 필수입니다.")
    @Password
    @Size(min = 8, max = 20 , message = "비밀번호는 8자 이상, 20자 이하입니다.")
    private String password;

    @NotBlank(message = "닉네임 값은 필수입니다.")
    @NoWhiteSpace
    @Size(max = 10, message = "닉네임은 최대 10자까지 작성 가능합니다.")
    private String nickname;

    private MultipartFile file;
}
