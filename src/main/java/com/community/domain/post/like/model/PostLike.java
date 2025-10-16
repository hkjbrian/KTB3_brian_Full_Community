package com.community.domain.post.like.model;

import com.community.domain.common.model.BaseTimeEntity;
import lombok.Getter;

@Getter
public class PostLike extends BaseTimeEntity {

    private final Long postId;
    private final Long userId;

    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
        markCreated();
    }
}
