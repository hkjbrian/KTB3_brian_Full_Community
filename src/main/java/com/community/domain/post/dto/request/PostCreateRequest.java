package com.community.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 26, message = "제목은 26자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "본문은 필수입니다.")
    private String body;

    @NotNull(message = "이미지 파일은 필수입니다.")
    private MultipartFile file;
}
