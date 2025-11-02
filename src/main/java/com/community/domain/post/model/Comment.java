package com.community.domain.post.model;

import com.community.domain.common.model.BaseTimeEntity;
import lombok.Getter;

@Getter
public class Comment extends BaseTimeEntity {

    private Long id;
    private final Long postId;
    private final Long userId;
    private String body;

    public Comment(Long postId, Long userId, String body) {
        this.postId = postId;
        this.userId = userId;
        this.body = body;
    }

    public void updateBody(String body) {
        this.body = body;
    }
}
