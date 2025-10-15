package com.community.domain.post.model;

import com.community.domain.common.model.BaseTimeEntity;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class Post extends BaseTimeEntity {

    private Long id;
    private final Long authorId;
    private String title;
    private String imageUrl;
    private String body;
    private final AtomicLong viewCount;
    private final AtomicLong likeCount;

    public Post(Long authorId, String title, String imageUrl, String body) {
        this.authorId = authorId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.body = body;
        this.viewCount = new AtomicLong(0);
        this.likeCount = new AtomicLong(0);
        markCreated();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void updateTitle(String title) {
        this.title = title;
        markUpdated();
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        markUpdated();
    }

    public void updateBody(String body) {
        this.body = body;
        markUpdated();
    }

    public void addViewCount() {
        this.viewCount.incrementAndGet();
    }

    public Long getViewCount() {
        return this.viewCount.get();
    }

    public Long getLikeCount() {
        return this.likeCount.get();
    }
}
