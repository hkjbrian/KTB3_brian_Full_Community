package com.community.domain.post.controller;

import com.community.domain.auth.annotation.Auth;
import com.community.domain.auth.annotation.AuthUser;
import com.community.domain.auth.dto.AuthenticatedUser;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.dto.response.PostIdResponse;
import com.community.domain.post.dto.response.PostListResponse;
import com.community.domain.post.dto.response.PostSingleResponse;
import com.community.domain.post.service.PostService;
import com.community.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<PostListResponse>> getPosts() {
        PostListResponse response = postService.getPostList();

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 목록 조회에 성공했습니다.", response));
    }

    @Auth
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostIdResponse>> createPost(@AuthUser AuthenticatedUser authenticatedUser,
                                                                  @ModelAttribute @Valid PostCreateRequest request) {
        PostIdResponse response = postService.createPost(authenticatedUser.userId(), request);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글이 등록되었습니다.", response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostSingleResponse>> getPost(@PathVariable Long postId) {
        PostSingleResponse response = postService.viewPost(postId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 상세 조회에 성공했습니다.", response));
    }

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

    @Auth
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,
                                                        @AuthUser AuthenticatedUser authenticatedUser) {
        postService.deletePost(postId, authenticatedUser.userId());
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글이 삭제되었습니다.", null));
    }
}
