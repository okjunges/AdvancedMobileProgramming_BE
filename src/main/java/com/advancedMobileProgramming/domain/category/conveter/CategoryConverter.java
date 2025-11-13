package com.advancedMobileProgramming.domain.category.conveter;

import com.advancedMobileProgramming.domain.category.dto.CategoryDtos;
import com.advancedMobileProgramming.domain.category.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryConverter {
    public static CategoryDtos.CategoryDto convertCategoryToCategoryDto(Category category) {
        return CategoryDtos.CategoryDto.builder().name(category.getName()).build();
    }

    public static CategoryDtos.CategoryListDto convertCategoryListToCategoryListDto(List<Category> categoryList) {
        int count = categoryList.size();
        List<CategoryDtos.CategoryDto> result = new ArrayList<>();
        for (Category category : categoryList) {
            result.add(CategoryDtos.CategoryDto.builder().name(category.getName()).build());
        }
        return CategoryDtos.CategoryListDto.builder().count(count).categories(result).build();
    }
}