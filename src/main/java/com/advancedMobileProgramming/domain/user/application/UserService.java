package com.advancedMobileProgramming.domain.user.application;

import com.advancedMobileProgramming.domain.user.dto.UserDtos;

public interface UserService {
    Long registerUser(UserDtos.RegisterRequestDto req);
    UserDtos.LoginResponseDto login(UserDtos.LoginRequestDto req);
    UserDtos.ProfileResponse getProfile(Long userId);
    void logout(String token);
}