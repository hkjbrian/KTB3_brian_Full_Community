package com.community.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentContent {

    private final Long id;
    private final String body;
    private final LocalDateTime updatedAt;
}
