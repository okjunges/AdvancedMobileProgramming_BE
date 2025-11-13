package com.advancedMobileProgramming.domain.category.application;

import com.advancedMobileProgramming.domain.category.conveter.CategoryConverter;
import com.advancedMobileProgramming.domain.category.dto.CategoryDtos;
import com.advancedMobileProgramming.domain.category.entity.Category;
import com.advancedMobileProgramming.domain.category.exception.ExistedCategoryException;
import com.advancedMobileProgramming.domain.category.repository.CategoryRepository;
import com.advancedMobileProgramming.domain.user.entity.User;
import com.advancedMobileProgramming.domain.user.entity.enums.Role;
import com.advancedMobileProgramming.domain.user.exception.UserNotAdmin;
import com.advancedMobileProgramming.domain.user.exception.UserNotFoundException;
import com.advancedMobileProgramming.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public CategoryDtos.CategoryDto addCategory(Long userId, String categoryName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        if (user.getRole() != Role.ADMIN)
            throw new UserNotAdmin();
        Category category = Category.builder().name(categoryName).build();
        if (categoryRepository.existsByName(categoryName))
            throw new ExistedCategoryException();
        categoryRepository.save(category);
        return CategoryConverter.convertCategoryToCategoryDto(category);
    }

    @Override
    public CategoryDtos.CategoryListDto getCategoryList() {
        List<Category> categoryList = categoryRepository.findAllByOrderByNameAsc();
        return CategoryConverter.convertCategoryListToCategoryListDto(categoryList);
    }
}