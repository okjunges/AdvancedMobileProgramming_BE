package com.advancedMobileProgramming.domain.user.entity;

import com.advancedMobileProgramming.domain.model.BaseEntity;
import com.advancedMobileProgramming.domain.user.entity.enums.Role;
import com.advancedMobileProgramming.domain.user.exception.UserWrongPasswordException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(name = "student_num", nullable = false, unique = true)
    private String studentNumber;

    @Column(name = "tell_num", nullable = false)
    private String tellNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String name, String email, String password, String studentNumber, String tellNumber, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentNumber = studentNumber;
        this.tellNumber = tellNumber;
        this.role = role;
    }

    // 로컬 회원가입
    public static User registerUser(String name, String email, String password, String studentNumber, String tellNumber, Role role) {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .studentNumber(studentNumber)
                .tellNumber(tellNumber)
                .role(role)
                .build();
    }

    public void verifyPassword(String rawPassword, PasswordEncoder encoder) {
        if (this.password == null | !encoder.matches(rawPassword, this.password)) {
            throw new UserWrongPasswordException();
        }
    }
}
