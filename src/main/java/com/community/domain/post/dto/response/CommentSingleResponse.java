package com.community.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentSingleResponse {

    private final CommentContent comment;
    private final AuthorResponse author;
}
