package com.community.domain.post.controller;

import com.community.domain.auth.annotation.Auth;
import com.community.domain.auth.annotation.AuthUser;
import com.community.domain.auth.dto.AuthenticatedUser;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.dto.response.PostIdResponse;
import com.community.domain.post.dto.response.PostLikeResponse;
import com.community.domain.post.dto.response.PostListResponse;
import com.community.domain.post.dto.response.PostSingleResponse;
import com.community.domain.post.service.PostService;
import com.community.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostApiSpec {

    private static final String URL_PREFIX = "/posts";

    @Value("${host}")
    private String HOST;
    private final PostService postService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<PostListResponse>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PostListResponse response = postService.getPostList(page, size);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 목록 조회에 성공했습니다.", response));
    }

    @Override
    @Auth
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostIdResponse>> createPost(@AuthUser AuthenticatedUser authenticatedUser,
                                                                  @ModelAttribute @Valid PostCreateRequest request) {
        PostIdResponse response = postService.createPost(authenticatedUser.userId(), request);

        return ResponseEntity
                .created(URI.create(HOST + URL_PREFIX + "/" + response.getId()))
                .body(ApiResponse.success("게시글이 등록되었습니다.", response));
    }

    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostSingleResponse>> getPost(@PathVariable Long postId) {
        PostSingleResponse response = postService.viewPost(postId);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 상세 조회에 성공했습니다.", response));
    }

    @Override
    @Auth
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostIdResponse>> updatePost(@PathVariable Long postId,
                                                                  @AuthUser AuthenticatedUser authenticatedUser,
                                                                  @ModelAttribute @Valid PostUpdateRequest request) {
        PostIdResponse response = postService.updatePost(postId, authenticatedUser.userId(), request);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글이 수정되었습니다.", response));
    }

    @Override
    @Auth
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,
                                                        @AuthUser AuthenticatedUser authenticatedUser) {
        postService.deletePost(postId, authenticatedUser.userId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success("게시글이 삭제되었습니다."));
    }

    @Override
    @Auth
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> toggleLike(@PathVariable Long postId,
                                                        @AuthUser AuthenticatedUser authenticatedUser) {
        PostLikeResponse response = postService.toggleLike(postId, authenticatedUser.userId());
        String message = response.isLiked() ? "게시글에 좋아요를 표시했습니다." : "게시글 좋아요가 취소되었습니다.";

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(message));
    }

    @Override
    @Auth
    @GetMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<PostLikeResponse>> isUserLikedPost(@PathVariable Long postId,
                                                                         @AuthUser AuthenticatedUser authenticatedUser) {
        PostLikeResponse res = postService.checkUserLikedPost(postId, authenticatedUser.userId());

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("유저의 게시글 좋아요 여부 조회에 성공했습니다.", res));
    }
}
