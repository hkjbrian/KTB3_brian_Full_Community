package com.community.domain.user.controller;

import com.community.global.response.ApiResponse;
import com.community.domain.user.dto.request.SignInRequest;
import com.community.domain.user.dto.response.SignInResponse;
import com.community.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@ModelAttribute @Valid SignInRequest req) {
        SignInResponse res = userService.signIn(req);
        return ResponseEntity
                .created(URI.create("/users/" + res.getId()))
                .body(ApiResponse.success("회원가입에 성공했습니다.", res));
    }
}
