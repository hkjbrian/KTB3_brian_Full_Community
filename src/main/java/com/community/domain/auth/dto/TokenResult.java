package com.community.domain.auth.dto;

import java.time.Instant;

public record TokenResult(
        String token,
        Instant expiresAt
) {}
