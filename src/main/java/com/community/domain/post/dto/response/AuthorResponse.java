package com.community.domain.post.dto.response;

import com.community.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorResponse {

    private final Long id;
    private final String nickname;
    private final String imageUrl;

    public static AuthorResponse from(User user) {
        return new AuthorResponse(user.getId(), user.getNickname(),  user.getImageUrl());
    }
}
