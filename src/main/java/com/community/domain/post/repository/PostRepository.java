package com.community.domain.post.repository;

import com.community.domain.post.model.Post;
import com.community.domain.user.model.User;

import java.util.Optional;

public interface PostRepository {

    Long save(Post post);
    void delete(Post post);
    Optional<Post> findById(Long postId);

}
