package com.community.domain.user.repository;

import com.community.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {

    Long save(User user);
    void delete(User user);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
}
