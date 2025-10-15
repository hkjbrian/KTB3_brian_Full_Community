package com.community.domain.auth;

import com.community.domain.auth.dto.TokenPayload;
import com.community.domain.auth.dto.TokenResult;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.access-expiration-seconds}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-expiration-seconds}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper;

    public TokenPayload parseAccessToken(String token) {
        TokenPayload payload = parseToken(token);
        if (!TOKEN_TYPE_ACCESS.equals(payload.type())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return payload;
    }

    public TokenPayload parseRefreshToken(String token) {
        TokenPayload payload = parseToken(token);
        if (!TOKEN_TYPE_REFRESH.equals(payload.type())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return payload;
    }

    public TokenResult createAccessToken(Long userId) {
        return createToken(userId, ACCESS_TOKEN_EXPIRATION_TIME, TOKEN_TYPE_ACCESS);
    }

    public TokenResult createRefreshToken(Long userId) {
        return createToken(userId, REFRESH_TOKEN_EXPIRATION_TIME, TOKEN_TYPE_REFRESH);
    }

    private TokenPayload parseToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            String unsignedToken = parts[0] + "." + parts[1];
            String expectedSignature = sign(unsignedToken);
            if (!constantTimeEquals(expectedSignature, parts[2])) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            Map<String, Object> payloadMap = objectMapper.readValue(
                    new String(BASE64_DECODER.decode(parts[1]), StandardCharsets.UTF_8),
                    new TypeReference<>() {
                    }
            );

            long expiresAt = ((Number) payloadMap.get("exp")).longValue();
            Instant expiryInstant = Instant.ofEpochSecond(expiresAt);
            if (Instant.now().isAfter(expiryInstant)) {
                throw new CustomException(ErrorCode.EXPIRED_TOKEN);
            }

            Long userId = Long.valueOf(payloadMap.get("sub").toString());
            String type = payloadMap.get("type").toString();
            return new TokenPayload(userId, expiryInstant, type);
        } catch (JsonProcessingException | IllegalArgumentException ex) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    private TokenResult createToken(Long userId, long expirationSeconds, String type) {
        try {
            Instant now = Instant.now();
            Instant expiresAt = now.plusSeconds(expirationSeconds);

            Map<String, Object> header = Map.of(
                    "alg", "HS256",
                    "typ", "JWT"
            );

            Map<String, Object> payload = Map.of(
                    "sub", userId,
                    "iat", now.getEpochSecond(),
                    "exp", expiresAt.getEpochSecond(),
                    "type", type
            );

            String encodedHeader = BASE64_ENCODER.encodeToString(objectMapper.writeValueAsBytes(header));
            String encodedPayload = BASE64_ENCODER.encodeToString(objectMapper.writeValueAsBytes(payload));
            String unsignedToken = encodedHeader + "." + encodedPayload;
            String signature = sign(unsignedToken);

            String token = unsignedToken + "." + signature;
            return new TokenResult(token, expiresAt);
        } catch (JsonProcessingException ex) {
            throw new CustomException(ErrorCode.TOKEN_GENERATION_ERROR);
        }
    }

    private String sign(String data) {
        try {
            Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            hmac.init(keySpec);
            byte[] signatureBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return BASE64_ENCODER.encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new IllegalStateException("Failed to sign JWT", ex);
        }
    }

    /**
     * 상수 시간에 서명을 비교하여 타이밍 공격 방지
     */
    private boolean constantTimeEquals(String expected, String actual) {
        if (expected.length() != actual.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expected.length(); i++) {
            result |= expected.charAt(i) ^ actual.charAt(i);
        }
        return result == 0;
    }
}
