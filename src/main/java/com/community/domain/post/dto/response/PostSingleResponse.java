package com.community.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSingleResponse {

    private final PostContent post;
    private final AuthorResponse author;
}
