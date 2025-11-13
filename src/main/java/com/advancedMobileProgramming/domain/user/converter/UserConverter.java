package com.advancedMobileProgramming.domain.user.converter;

import com.advancedMobileProgramming.domain.user.dto.UserDtos;
import com.advancedMobileProgramming.domain.user.entity.User;

public class UserConverter {
    public static UserDtos.ProfileResponse toProfileResponse(User user) {
        return UserDtos.ProfileResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .studentNum(user.getStudentNumber())
                .name(user.getName())
                .tellNum(user.getTellNumber())
                .role(user.getRole().name())
                .build();
    }

    public static UserDtos.LoginResponseDto toLoginResponseDto(User user, String accessToken, String refreshToken) {
        return UserDtos.LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .me(toProfileResponse(user))
                .build();
    }
}