package com.community.domain.user.dto.request;

import com.community.global.validation.annotation.AtLeastOneField;
import com.community.global.validation.annotation.NoWhiteSpace;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import static com.community.global.validation.MessageConstants.NICKNAME_SIZE_INVALID;
import static com.community.global.validation.MessageConstants.PROFILE_UPDATE_REQUIRED;

@Data
@AtLeastOneField(message = PROFILE_UPDATE_REQUIRED, fields = {"nickname", "file"})
public class UpdateRequest {

    @Nullable
    @NoWhiteSpace
    @Size(max = 10, message = NICKNAME_SIZE_INVALID)
    private String nickname;

    @Nullable
    private MultipartFile file;
}
