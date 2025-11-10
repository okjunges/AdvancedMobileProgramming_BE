package com.advancedMobileProgramming.domain.user.api;

import com.advancedMobileProgramming.domain.user.application.UserService;
import com.advancedMobileProgramming.domain.user.dto.UserDtos;
import com.advancedMobileProgramming.global.common.code.status.SuccessStatus;
import com.advancedMobileProgramming.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody @Valid UserDtos.RegisterRequestDto req) {
        Long id = userService.registerUser(req);
        return BaseResponse.onSuccess(SuccessStatus.USER_REGISTER_LOCAL_SUCCESS, id);
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<UserDtos.LoginResponseDto> login(@RequestBody @Valid UserDtos.LoginRequestDto req) {
        UserDtos.LoginResponseDto result = userService.login(req);
        return BaseResponse.onSuccess(SuccessStatus.USER_LOGIN_LOCAL_SUCCESS, result);
    }

    // 정보 조회
    @GetMapping("/me")
    public BaseResponse<UserDtos.ProfileResponse> me(@AuthenticationPrincipal Long userId) {
        UserDtos.ProfileResponse result = userService.getProfile(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_SUCCESS, result);
    }

    // 로그아웃
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = authorization.substring(7);
        userService.logout(token);
        return BaseResponse.onSuccess(SuccessStatus.USER_LOGOUT_SUCCESS, null);
    }
}