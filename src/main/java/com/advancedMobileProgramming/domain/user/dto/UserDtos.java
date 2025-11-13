package com.advancedMobileProgramming.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class UserDtos {
    // 회원가입 요청
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequestDto {
        @NotBlank String name;
        @Email @NotBlank String email;
        @NotBlank String password;
        @NotBlank String studentNum;
        @NotBlank String tellNum;
    }

    // 로그인 요청
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDto {
        @NotBlank String studentNum;
        @NotBlank String password;
    }

    // 로그인 응답
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private String accessToken;
        private String refreshToken;
        private String tokenType; // "Bearer"
        private ProfileResponse me;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileResponse {
        private Long userId;
        private String email;
        private String studentNum;
        private String name;
        private String tellNum;
        private String role;
    }
}