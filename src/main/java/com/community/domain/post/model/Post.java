package com.community.domain.post.model;

import com.community.domain.common.model.BaseTimeEntity;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class Post extends BaseTimeEntity {

    private Long id;
    private Long authorId;
    private String title;
    private String imageUrl;
    private String body;
    private AtomicLong viewCount;

    public Post(Long authorId, String title, String imageUrl, String body) {
        this.authorId = authorId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.body = body;
        this.viewCount = new AtomicLong(0);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void addViewCount() {
        this.viewCount.incrementAndGet();
    }
}
