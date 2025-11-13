package com.advancedMobileProgramming.domain.category.repository;

import com.advancedMobileProgramming.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
    boolean existsByName(String name);
    List<Category> findAllByOrderByNameAsc();
}