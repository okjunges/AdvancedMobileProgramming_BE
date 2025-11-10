package com.advancedMobileProgramming.domain.user.repository;

import com.advancedMobileProgramming.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByStudentNumber(String studentNum);
    Optional<User> findByStudentNumber(String studentNum);
}
