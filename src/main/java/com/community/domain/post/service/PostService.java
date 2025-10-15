package com.community.domain.post.service;

import com.community.domain.file.service.FileStorageService;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.dto.response.*;
import com.community.domain.post.model.Post;
import com.community.domain.post.repository.PostRepository;
import com.community.domain.user.model.User;
import com.community.domain.user.repository.UserRepository;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    private static final Long commentCount = 0L;

    public PostListResponse getPostList() {
        List<PostSingleResponse> postList =
                postRepository.findAll().stream()
                        .map(post -> new PostSingleResponse(
                                PostContent.from(post, commentCount),
                                getAuthorResponse(post.getAuthorId())
                        ))
                        .toList();

        return new PostListResponse(postList);
    }

    public PostSingleResponse viewPost(Long postId) {
        Post post = findPost(postId);
        post.addViewCount();
        User user = userRepository.findById(post.getAuthorId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return new PostSingleResponse(PostContent.from(post, commentCount), getAuthorResponse(post.getAuthorId()));
    }

    public PostIdResponse createPost(Long authorId, PostCreateRequest req) {
        String imageUrl = fileStorageService.save(req.getFile());
        Post post = new Post(authorId, req.getTitle(), imageUrl, req.getBody());
        Long id = postRepository.save(post);

        return new PostIdResponse(id);
    }

    public PostIdResponse updatePost(Long postId, Long authorId, PostUpdateRequest request) {
        Post post = findPost(postId);
        validateAuthor(post, authorId);

        post.updateTitle(request.getTitle());
        post.updateBody(request.getBody());

        MultipartFile image = request.getFile();
        if (image != null && !image.isEmpty()) {
            String previousImageUrl = post.getImageUrl();
            String imageUrl = fileStorageService.save(image);
            post.updateImageUrl(imageUrl);
            fileStorageService.delete(previousImageUrl);
        }

        postRepository.save(post);
        return new PostIdResponse(post.getId());
    }

    public void deletePost(Long postId, Long authorId) {
        Post post = findPost(postId);
        validateAuthor(post, authorId);
        fileStorageService.delete(post.getImageUrl());
        postRepository.delete(post);
    }

    private AuthorResponse getAuthorResponse(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return AuthorResponse.from(user);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateAuthor(Post post, Long authorId) {
        if (!post.getAuthorId().equals(authorId)) {
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }
    }
}
