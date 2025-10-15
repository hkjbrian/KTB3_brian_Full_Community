package com.community.domain.auth.dto.request;

import com.community.global.validation.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.community.global.validation.MessageConstants.*;

@Data
public class LoginRequest {

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_FORMAT_INVALID)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Password
    @Size(min = 8, max = 20, message = PASSWORD_SIZE_INVALID)
    private String password;
}
