package com.community.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentListResponse {

    private final List<CommentSingleResponse> commentList;
}
