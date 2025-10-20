package com.community.domain.post.controller;

import com.community.domain.auth.annotation.Auth;
import com.community.domain.auth.annotation.AuthUser;
import com.community.domain.auth.dto.AuthenticatedUser;
import com.community.domain.post.dto.request.CommentRequest;
import com.community.domain.post.dto.response.CommentIdResponse;
import com.community.domain.post.dto.response.CommentListResponse;
import com.community.domain.post.service.CommentService;
import com.community.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Comment", description = "댓글 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private static final String URL_PREFIX = "/posts/{postId}/comments";

    @Value("${host}")
    private static String HOST;

    private final CommentService commentService;

    @Operation(summary = "댓글 리스트 조회", description = "페이징을 기반으로 댓글 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<CommentListResponse>> getComments(@PathVariable Long postId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        CommentListResponse res = commentService.getComments(postId, page, size);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 댓글 조회에 성공했습니다.", res));
    }

    @Operation(summary = "댓글 추가", description = "특정 게시글에 댓글을 작성합니다.")
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

    @Operation(summary = "댓글 수정", description = "특정 게시글에 달려있는 특정 댓글을 수정합니다.")
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

    @Operation(summary = "댓글 삭제", description = "특정 게시글에 달려있는 특정 댓글을 삭제합니다.")
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
