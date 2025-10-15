package com.community.domain.user.dto.request;

import com.community.global.validation.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.community.global.validation.MessageConstants.PASSWORD_REQUIRED;
import static com.community.global.validation.MessageConstants.PASSWORD_SIZE_INVALID;

@Data
public class PasswordUpdateRequest {

    @NotBlank(message = PASSWORD_REQUIRED)
    private String currentPassword;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Password
    @Size(min = 8, max = 20, message = PASSWORD_SIZE_INVALID)
    private String newPassword;
}
