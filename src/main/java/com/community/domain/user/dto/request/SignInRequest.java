package com.community.domain.user.dto.request;

import com.community.global.validation.annotation.NoWhiteSpace;
import com.community.global.validation.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.community.global.validation.MessageConstants.*;

@Schema(description = "회원가입 요청 DTO")
@Data
public class SignInRequest {

    @Schema(description = "이메일 주소", example = "email@email.com")
    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_FORMAT_INVALID)
    private String email;

    @Schema(description = "비밀번호", example = "a1234567890A!")
    @NotBlank(message = PASSWORD_REQUIRED)
    @Password
    @Size(min = 8, max = 20, message = PASSWORD_SIZE_INVALID)
    private String password;

    @Schema(description ="닉네임", example = "nickname")
    @NotBlank(message = NICKNAME_REQUIRED)
    @NoWhiteSpace
    @Size(max = 10, message = NICKNAME_SIZE_INVALID)
    private String nickname;

    @Schema(description = "이미지 파일")
    private MultipartFile file;
}
