package com.community.domain.post.service;

import com.community.domain.post.dto.request.CommentRequest;

import com.community.domain.post.dto.response.*;

import com.community.domain.post.model.Comment;
import com.community.domain.post.repository.CommentRepository;
import com.community.domain.post.repository.PostRepository;
import com.community.domain.user.model.User;
import com.community.domain.user.repository.UserRepository;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentListResponse getComments(Long postId) {
        ensurePostExists(postId);

        List<CommentSingleResponse> items = commentRepository.findByPostId(postId).stream()
                .map(this::toSingleResponse)
                .toList();

        return new CommentListResponse(items);
    }

    public CommentIdResponse createComment(Long postId, Long authorId, CommentRequest request) {
        ensurePostExists(postId);

        Comment comment = new Comment(postId, authorId, request.getBody());
        Long id = commentRepository.save(comment);
        return new CommentIdResponse(id);
    }

    public CommentIdResponse updateComment(Long postId, Long commentId, Long authorId, CommentRequest request) {
        Comment comment = findComment(commentId);
        validateCommentPost(comment, postId);
        validateCommentAuthor(comment, authorId);

        comment.updateBody(request.getBody());
        commentRepository.save(comment);
        return new CommentIdResponse(comment.getId());
    }

    public void deleteComment(Long postId, Long commentId, Long authorId) {
        Comment comment = findComment(commentId);
        validateCommentPost(comment, postId);
        validateCommentAuthor(comment, authorId);

        commentRepository.delete(comment);
    }

    public Long countComments(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    private void ensurePostExists(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateCommentPost(Comment comment, Long postId) {
        if (!comment.getPostId().equals(postId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    private void validateCommentAuthor(Comment comment, Long authorId) {
        if (!comment.getUserId().equals(authorId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }
    }

    private CommentSingleResponse toSingleResponse(Comment comment) {
        CommentContent commentContent = new CommentContent(
                comment.getId(),
                comment.getBody(),
                comment.getUpdatedAt());

        AuthorResponse authorResponse = toAuthor(comment.getUserId());

        return new CommentSingleResponse(commentContent, authorResponse);
    }

    private AuthorResponse toAuthor(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new AuthorResponse(user.getId(), user.getNickname(), user.getImageUrl());
    }
}
