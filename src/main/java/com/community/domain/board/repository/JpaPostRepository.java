package com.community.domain.board.repository;

import com.community.domain.board.model.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class JpaPostRepository implements PostRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long save(Post post) {
        em.persist(post);

        return post.getId();
    }

    @Override
    public void delete(Post post) {
        em.remove(post);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return Optional.ofNullable(em.find(Post.class, postId));
    }

    @Override
    public List<Post> findAll() {
        return em.createQuery("select p from Post p order by createdAt desc ", Post.class).getResultList();
    }

    @Override
    public List<Post> findAllByUserId(Long userId) {
        return em.createQuery("select p from Post p join fetch p.user u where u.id = :userId", Post.class).
                setParameter("userId", userId).getResultList();
    }
}
