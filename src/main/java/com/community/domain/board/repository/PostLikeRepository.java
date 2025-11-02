package com.community.domain.board.repository;

public interface PostLikeRepository {

    void save(Long postId, Long userId);

    void delete(Long postId, Long userId);

    void deleteAllByPostId(Long postId);

    boolean exists(Long postId, Long userId);

    long countByPostId(Long postId);
}
