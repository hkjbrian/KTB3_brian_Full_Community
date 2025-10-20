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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "User", description = "회원 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원 가입을 요청합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@ModelAttribute @Valid SignInRequest req) {
        SignInResponse res = userService.signIn(req);
        return ResponseEntity
                .created(URI.create("/users/" + res.getId()))
                .body(ApiResponse.success("회원가입에 성공했습니다.", res));
    }

    @Operation(summary = "회원가입 정보 검증", description = "회원 가입에 필요한 정보( 이메일, 닉네임 ) 사용 가능 여부를 검증합니다.")
    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<SignInAvailableResponse>> checkAvailableSignInInfo(@RequestParam @Nullable String email,
                                                                                         @RequestParam @Nullable String nickname) {
        SignInAvailableResponse res = userService.checkAvailableSignInInfo(email, nickname);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("사용할 수 있는 정보입니다.", res));
    }

    @Operation(summary = "회원 정보 조회", description = "본인 회원 정보를 조회합니다.")
    @Auth
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(@AuthUser AuthenticatedUser authenticatedUser) {
        UserResponse profile = userService.getUserProfile(authenticatedUser.userId());
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("사용자 정보 조회에 성공했습니다.", profile));
    }

    @Operation(summary = "회원 정보 수정", description = "본인 회원 정보를 수정합니다.")
    @Auth
    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateProfile(@AuthUser AuthenticatedUser authenticatedUser,
                                                           @ModelAttribute @Valid UpdateRequest req) {
        userService.updateProfile(authenticatedUser.userId(), req);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("사용자 정보가 수정되었습니다.", null));
    }

    @Operation(summary = "회원 비밀번호 수정", description = "본인 비밀번호를 수정합니다.")
    @Auth
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthUser AuthenticatedUser authenticatedUser,
                                                            @RequestBody @Valid PasswordUpdateRequest req) {
        userService.changePassword(authenticatedUser.userId(), req);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("비밀번호가 수정되었습니다.", null));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다. 관련된 정보를 삭제합니다.")
    @Auth
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthUser AuthenticatedUser authenticatedUser) {
        userService.deleteUser(authenticatedUser.userId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
