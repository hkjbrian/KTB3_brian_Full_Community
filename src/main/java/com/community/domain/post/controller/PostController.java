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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Post", description = "게시글 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private static final String URL_PREFIX = "/posts";

    @Value("${host}")
    private static String HOST;
    private final PostService postService;

    @Operation(summary = "게시글 리스트 조회", description = "페이징 기능을 바탕으로 게시글 리스트를 조회합니다.")
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

    @Operation(summary = "게시글 생성", description = "게시글을 작성하여 게시판에 등록합니다.")
    @Auth
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostIdResponse>> createPost(@AuthUser AuthenticatedUser authenticatedUser,
                                                                  @ModelAttribute @Valid PostCreateRequest request) {
        PostIdResponse response = postService.createPost(authenticatedUser.userId(), request);

        return ResponseEntity
                .created(URI.create(HOST + URL_PREFIX + "/" + response.getId()))
                .body(ApiResponse.success("게시글이 등록되었습니다.", response));
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글을 단건 조회합니다. 조회수가 증가합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostSingleResponse>> getPost(@PathVariable Long postId) {
        PostSingleResponse response = postService.viewPost(postId);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("게시글 상세 조회에 성공했습니다.", response));
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
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

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 관련된 댓글도 함께 삭제됩니다.")
    @Auth
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,
                                                        @AuthUser AuthenticatedUser authenticatedUser) {
        postService.deletePost(postId, authenticatedUser.userId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success("게시글이 삭제되었습니다."));
    }

    @Operation(summary = "게시글 좋아요 토글", description = "게시글의 좋아요를 토글로 표현합니다.")
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

    @Operation(summary = "게시글 좋아요 여부 조회", description = "현재 회원이 특정 게시글에 좋아요를 누른 상태인지 조회합니다.")
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
