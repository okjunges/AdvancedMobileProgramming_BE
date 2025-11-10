package com.advancedMobileProgramming.domain.user.application;

import com.advancedMobileProgramming.domain.user.converter.UserConverter;
import com.advancedMobileProgramming.domain.user.dto.UserDtos;
import com.advancedMobileProgramming.domain.user.entity.User;
import com.advancedMobileProgramming.domain.user.entity.enums.Role;
import com.advancedMobileProgramming.domain.user.exception.StudentAlreadyUseException;
import com.advancedMobileProgramming.domain.user.exception.UserNotFoundException;
import com.advancedMobileProgramming.domain.user.repository.UserRepository;
import com.advancedMobileProgramming.global.util.jtw.InMemoryTokenBlacklist;
import com.advancedMobileProgramming.global.util.jtw.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final InMemoryTokenBlacklist blacklist;

    @Override
    public Long registerUser(UserDtos.RegisterRequestDto req) {
        if (userRepository.existsByStudentNumber(req.getStudentNum())) {
            throw new StudentAlreadyUseException();
        }

        User user = User.registerUser(req.getName(), req.getEmail(), passwordEncoder.encode(req.getPassword()),
                req.getStudentNum(), req.getTellNum(), Role.USER);
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtos.LoginResponseDto login(UserDtos.LoginRequestDto req) {
        User user = userRepository.findByStudentNumber(req.getStudentNum())
                .orElseThrow(UserNotFoundException::new);

        user.verifyPassword(req.getPassword(), passwordEncoder);
        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getStudentNumber(), user.getRole().name());

        return UserConverter.toLoginResponseDto(user, token);
    }

    @Override
    public UserDtos.ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        return UserConverter.toProfileResponse(user);
    }

    @Override
    public void logout(String token) {
        var exp = jwtTokenProvider.getExpiration(token);
        blacklist.blacklist(token, exp);
    }
}
