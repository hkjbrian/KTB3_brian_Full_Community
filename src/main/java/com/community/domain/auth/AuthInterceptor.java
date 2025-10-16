package com.community.domain.auth;

import com.community.domain.auth.annotation.Auth;
import com.community.domain.auth.dto.TokenPayload;
import com.community.domain.auth.service.AuthService;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if (!requiresAuthentication(handlerMethod)) {
            return true;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        String accessToken = authorization.substring(7);
        TokenPayload payload = authService.verifyAndParseAccessToken(accessToken);
        request.setAttribute(AuthConstants.AUTHENTICATED_USER_ID, payload.userId());
        return true;
    }

    private boolean requiresAuthentication(HandlerMethod handlerMethod) {
        return handlerMethod.getMethod().isAnnotationPresent(Auth.class)
                || handlerMethod.getBeanType().isAnnotationPresent(Auth.class);
    }
}
