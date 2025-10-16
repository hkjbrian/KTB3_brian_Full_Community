package com.community.domain.post.controller;

import com.community.domain.auth.annotation.Auth;
import com.community.domain.auth.annotation.AuthUser;
import com.community.domain.auth.dto.AuthenticatedUser;
import com.community.domain.post.dto.request.CommentRequest;
import com.community.domain.post.dto.response.CommentIdResponse;
import com.community.domain.post.dto.response.CommentListResponse;
import com.community.domain.post.service.CommentService;
import com.community.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private static final String URL_PREFIX = "/posts/{postId}/comments";

    @Value("${host}")
    private static String HOST;

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<CommentListResponse>> getComments(@PathVariable Long postId) {
        CommentListResponse res = commentService.getComments(postId);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 댓글 조회에 성공했습니다.", res));
    }

    @Auth
    @PostMapping
    public ResponseEntity<ApiResponse<CommentIdResponse>> createComment(@PathVariable Long postId,
                                                                        @AuthUser AuthenticatedUser authenticatedUser,
                                                                        @RequestBody @Valid CommentRequest request) {
        CommentIdResponse res = commentService.createComment(postId, authenticatedUser.userId(), request);

        return ResponseEntity
                .created(URI.create(HOST + URL_PREFIX.replace("{postId}", postId.toString()) + "/" + res.getId()))
                .body(ApiResponse.success("게시글 댓글 등록에 성공했습니다.", res));
    }

    @Auth
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentIdResponse>> updateComment(@PathVariable Long postId,
                                                                        @PathVariable Long commentId,
                                                                        @AuthUser AuthenticatedUser authenticatedUser,
                                                                        @RequestBody @Valid CommentRequest request) {
        CommentIdResponse response = commentService.updateComment(postId, commentId, authenticatedUser.userId(), request);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 댓글 수정에 성공했습니다.", response));
    }

    @Auth
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long postId,
                                                           @PathVariable Long commentId,
                                                           @AuthUser AuthenticatedUser authenticatedUser) {
        commentService.deleteComment(postId, commentId, authenticatedUser.userId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success("게시글 댓글 삭제에 성공했습니다."));
    }
}
