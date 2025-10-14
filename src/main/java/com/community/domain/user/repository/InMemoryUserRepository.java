package com.community.domain.user.repository;

import com.community.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private static final Map<Long, User> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Long save(User user) {
        if (user.getId() == null) {
            long newId = sequence.incrementAndGet();
            user.setId(newId);
        }
        store.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public void delete(User user) {
        store.remove(user.getId());
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByNickName(String nickName) {
        return store.values().stream()
                .filter(user -> user.getNickname().equals(nickName))
                .findFirst();
    }
}