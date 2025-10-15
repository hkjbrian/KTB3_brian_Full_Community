package com.community.domain.user.dto.request;

import com.community.global.validation.annotation.NoWhiteSpace;
import com.community.global.validation.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.community.global.validation.MessageConstants.*;


@Data
public class SignInRequest {

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_FORMAT_INVALID)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Password
    @Size(min = 8, max = 20, message = PASSWORD_SIZE_INVALID)
    private String password;

    @NotBlank(message = NICKNAME_REQUIRED)
    @NoWhiteSpace
    @Size(max = 10, message = NICKNAME_SIZE_INVALID)
    private String nickname;

    private MultipartFile file;
}
