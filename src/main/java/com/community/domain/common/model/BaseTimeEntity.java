package com.community.domain.common.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseTimeEntity {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
