package com.community.domain.post.repository;

import com.community.domain.post.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Long save(Post post);
    void delete(Post post);
    Optional<Post> findById(Long postId);
    List<Post> findAll();

}
