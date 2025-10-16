package com.community.domain.post.service;

import com.community.domain.file.service.FileStorageService;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.dto.response.*;
import com.community.domain.post.like.repository.PostLikeRepository;
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

import static com.community.domain.common.util.PageUtil.paginate;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final FileStorageService fileStorageService;
    private final CommentService commentService;

    public PostListResponse getPostList(int page, int size) {
        List<Post> posts = postRepository.findAll();
        List<Post> pagedPosts = paginate(posts, page, size);
        List<PostSingleResponse> postList = pagedPosts.stream()
                .map(this::toSingleResponse)
                .toList();

        return new PostListResponse(postList);
    }

    public PostSingleResponse viewPost(Long postId) {
        Post post = findPost(postId);
        post.addViewCount();
        postRepository.save(post);

        return toSingleResponse(post);
    }

    public PostIdResponse createPost(Long userId, PostCreateRequest req) {
        String imageUrl = fileStorageService.save(req.getFile());
        Post post = new Post(userId, req.getTitle(), imageUrl, req.getBody());
        Long id = postRepository.save(post);

        return new PostIdResponse(id);
    }

    public PostIdResponse updatePost(Long postId, Long userId, PostUpdateRequest request) {
        Post post = findPost(postId);
        validateAuthor(post, userId);

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

    public void deletePost(Long postId, Long userId) {
        Post post = findPost(postId);
        validateAuthor(post, userId);
        fileStorageService.delete(post.getImageUrl());
        postLikeRepository.deleteAllByPostId(postId);
        commentService.deleteAllCommentByPostId(postId);
        postRepository.delete(post);
    }

    public void deleteAllPostByUserId(Long userId) {
        List<Post> postList = postRepository.findAll().stream()
                .filter(post -> post.getUserId().equals(userId))
                .toList();

        for (Post post : postList) {
            deletePost(post.getId(), userId);
        }
    }

    public PostLikeResponse toggleLike(Long postId, Long userId) {
        findPost(postId);
        boolean alreadyLiked = postLikeRepository.exists(postId, userId);

        if (alreadyLiked) {
            postLikeRepository.delete(postId, userId);
        } else {
            postLikeRepository.save(postId, userId);
        }

        return new PostLikeResponse(!alreadyLiked);
    }

    public PostLikeResponse checkUserLikedPost(Long postId, Long userId) {
        findPost(postId);
        boolean alreadyLiked = postLikeRepository.exists(postId, userId);

        return new PostLikeResponse(alreadyLiked);
    }

    private AuthorResponse getAuthorResponse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return AuthorResponse.from(user);
    }

    private PostSingleResponse toSingleResponse(Post post) {
        long likeCount = postLikeRepository.countByPostId(post.getId());

        Long commentCount = commentService.countComments(post.getId());

        return new PostSingleResponse(
                PostContent.from(post, likeCount, commentCount),
                getAuthorResponse(post.getUserId())
        );
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateAuthor(Post post, Long userId) {
        if (!post.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }
    }
}
