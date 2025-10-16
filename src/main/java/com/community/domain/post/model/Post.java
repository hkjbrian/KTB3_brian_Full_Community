package com.community.domain.post.model;

import com.community.domain.common.model.BaseTimeEntity;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class Post extends BaseTimeEntity {

    private Long id;
    private final Long userId;
    private String title;
    private String imageUrl;
    private String body;
    private final AtomicLong viewCount;

    public Post(Long userId, String title, String imageUrl, String body) {
        this.userId = userId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.body = body;
        this.viewCount = new AtomicLong(0);
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

    public long getViewCount() {
        return this.viewCount.get();
    }
}
