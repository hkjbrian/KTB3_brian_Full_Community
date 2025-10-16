package com.community.domain.post.repository;

import com.community.domain.post.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCommentRepository implements CommentRepository {

    private static final Map<Long, Comment> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Long save(Comment comment) {
        if (comment.getId() == null) {
            long newId = sequence.incrementAndGet();
            comment.setId(newId);
        }
        store.put(comment.getId(), comment);
        return comment.getId();
    }

    @Override
    public void delete(Comment comment) {
        store.remove(comment.getId());
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return Optional.ofNullable(store.get(commentId));
    }

    @Override
    public List<Comment> findByPostId(Long postId) {

        return store.values().stream()
                .filter(comment -> Objects.equals(comment.getPostId(), postId))
                .toList();
    }

    @Override
    public long countByPostId(Long postId) {
        return store.values().stream()
                .filter(comment -> Objects.equals(comment.getPostId(), postId))
                .count();
    }

    @Override
    public void deleteByPostId(Long postId) {
        store.values().removeIf(comment -> Objects.equals(comment.getPostId(), postId));
    }
}
