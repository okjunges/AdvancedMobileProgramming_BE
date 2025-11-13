package com.advancedMobileProgramming.domain.user.application;

import com.advancedMobileProgramming.domain.user.converter.UserConverter;
import com.advancedMobileProgramming.domain.user.dto.UserDtos;
import com.advancedMobileProgramming.domain.user.entity.User;
import com.advancedMobileProgramming.domain.user.entity.enums.Role;
import com.advancedMobileProgramming.domain.user.exception.StudentAlreadyUseException;
import com.advancedMobileProgramming.domain.user.exception.UnauthorizedException;
import com.advancedMobileProgramming.domain.user.exception.UserNotFoundException;
import com.advancedMobileProgramming.domain.user.repository.UserRepository;
import com.advancedMobileProgramming.global.util.jtw.InMemoryTokenBlacklist;
import com.advancedMobileProgramming.global.util.jtw.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getRole().name());

        return UserConverter.toLoginResponseDto(user, token, refreshToken);
    }

    @Override
    public String refresh(String refreshToken) {
        if (blacklist.isBlacklisted(refreshToken)) {
            throw new UnauthorizedException();
        }
        String newAccessToken = "";
        try {
            Jws<Claims> jws = jwtTokenProvider.parse(refreshToken);
            Long userId = Long.valueOf(jws.getPayload().getSubject());
            String role = jws.getPayload().get("role", String.class);

            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

            newAccessToken = jwtTokenProvider.createAccessToken(userId, user.getStudentNumber(), role);
        }
        catch (JwtException e) { throw new UnauthorizedException(); }
        return newAccessToken;
    }

    @Override
    public UserDtos.ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        return UserConverter.toProfileResponse(user);
    }

    @Override
    public void logout(String token, String refreshToken) {
        var exp = jwtTokenProvider.getExpiration(token);
        var refreshExp = jwtTokenProvider.getExpiration(refreshToken);
        blacklist.blacklist(token, exp);
        blacklist.blacklist(refreshToken, refreshExp);
    }
}
