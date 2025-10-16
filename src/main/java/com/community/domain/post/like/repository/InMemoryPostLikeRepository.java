package com.community.domain.post.like.repository;

import com.community.domain.post.like.model.PostLike;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPostLikeRepository implements PostLikeRepository {

    private static final Map<String, PostLike> store = new ConcurrentHashMap<>();

    @Override
    public void save(Long postId, Long userId) {
        store.put(key(postId, userId), new PostLike(postId, userId));
    }

    @Override
    public void delete(Long postId, Long userId) {
        store.remove(key(postId, userId));
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        store.entrySet().removeIf(entry -> entry.getValue().getPostId().equals(postId));
    }

    @Override
    public boolean exists(Long postId, Long userId) {
        return store.containsKey(key(postId, userId));
    }

    @Override
    public long countByPostId(Long postId) {
        return store.values().stream()
                .filter(like -> like.getPostId().equals(postId))
                .count();
    }

    private String key(Long postId, Long userId) {
        return postId + ":" + userId;
    }
}
