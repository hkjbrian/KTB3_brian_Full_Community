package com.community.domain.auth.service;

import com.community.domain.auth.dto.LoginResult;
import com.community.domain.auth.dto.request.LoginRequest;

public interface AuthService {

    LoginResult login(LoginRequest req);

    LoginResult refresh(String token);
}
