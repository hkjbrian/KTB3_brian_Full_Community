package com.community.domain.user.controller;

import com.community.domain.auth.annotation.Auth;
import com.community.domain.auth.annotation.AuthUser;
import com.community.domain.auth.dto.AuthenticatedUser;
import com.community.domain.user.dto.request.PasswordUpdateRequest;
import com.community.domain.user.dto.request.SignInRequest;
import com.community.domain.user.dto.request.UpdateRequest;
import com.community.domain.user.dto.response.SignInAvailableResponse;
import com.community.domain.user.dto.response.SignInResponse;
import com.community.domain.user.dto.response.UserResponse;
import com.community.domain.user.service.UserService;
import com.community.global.response.ApiResponse;
import jakarta.annotation.Nullable;
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

    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<SignInAvailableResponse>> checkAvailableSignInInfo(@RequestParam @Nullable String email,
                                                                                         @RequestParam @Nullable String nickname) {
        SignInAvailableResponse res = userService.checkAvailableSignInInfo(email, nickname);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("사용할 수 있는 정보입니다.", res));
    }

    @Auth
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(@AuthUser AuthenticatedUser authenticatedUser) {
        UserResponse profile = userService.getUserProfile(authenticatedUser.userId());
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("사용자 정보 조회에 성공했습니다.", profile));
    }

    @Auth
    @PatchMapping(value = "/me")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@AuthUser AuthenticatedUser authenticatedUser,
                                                           @ModelAttribute @Valid UpdateRequest req) {
        userService.updateProfile(authenticatedUser.userId(), req);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("사용자 정보가 수정되었습니다.", null));
    }

    @Auth
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthUser AuthenticatedUser authenticatedUser,
                                                            @RequestBody @Valid PasswordUpdateRequest req) {
        userService.changePassword(authenticatedUser.userId(), req);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("비밀번호가 수정되었습니다.", null));
    }

    @Auth
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deletePassword(@AuthUser AuthenticatedUser authenticatedUser) {
        userService.deleteUser(authenticatedUser.userId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
