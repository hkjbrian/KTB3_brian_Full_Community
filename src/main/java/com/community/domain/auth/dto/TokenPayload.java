package com.community.domain.auth.dto;

import java.time.Instant;

public record TokenPayload(
        Long userId,
        Instant expiresAt,
        String type
) { }
