package com.advancedMobileProgramming.domain.category.application;

import com.advancedMobileProgramming.domain.category.dto.CategoryDtos;

public interface CategoryService {
    CategoryDtos.CategoryDto addCategory(Long userId, String categoryName);
    CategoryDtos.CategoryListDto getCategoryList();
}