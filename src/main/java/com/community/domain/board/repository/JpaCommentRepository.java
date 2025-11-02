package com.community.domain.board.repository;

import com.community.domain.board.model.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long save(Comment comment) {
        em.persist(comment);

        return comment.getId();
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId).getResultList();
    }

    @Override
    public Long countByPostId(Long postId) {
        return em.createQuery("select count(c) from Comment c where c.post.id = :postId", Long.class)
                .setParameter("postId", postId).getSingleResult();
    }

    @Override
    public void deleteByPostId(Long postId) {
        em.createQuery("delete from Comment c where c.post.id = :postId")
                .executeUpdate();
    }
}
