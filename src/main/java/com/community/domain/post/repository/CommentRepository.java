package com.community.domain.post.repository;

import com.community.domain.post.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Long save(Comment comment);
    void delete(Comment comment);
    Optional<Comment> findById(Long commentId);
    List<Comment> findByPostId(Long postId);
    long countByPostId(Long postId);
    void deleteByPostId(Long postId);
}
