package com.community.domain.post.repository;

import com.community.domain.post.model.Post;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPostRepository implements PostRepository {

    private static final Map<Long, Post> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Long save(Post post) {
        if (post.getId() == null) {
            long newId = sequence.incrementAndGet();
            post.setId(newId);
        }
        store.put(post.getId(), post);
        return post.getId();
    }

    @Override
    public void delete(Post post) {
        store.remove(post.getId());
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return Optional.ofNullable(store.get(postId));
    }
}
