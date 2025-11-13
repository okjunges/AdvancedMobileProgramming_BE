package com.advancedMobileProgramming.domain.category.api;

import com.advancedMobileProgramming.domain.category.application.CategoryService;
import com.advancedMobileProgramming.domain.category.dto.CategoryDtos;
import com.advancedMobileProgramming.global.common.code.status.SuccessStatus;
import com.advancedMobileProgramming.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;

    // 카테고리 추가
    @PostMapping("/add")
    public BaseResponse<CategoryDtos.CategoryDto> addCategory(@AuthenticationPrincipal Long userId, @RequestParam("name") String name) {
        CategoryDtos.CategoryDto result = categoryService.addCategory(userId, name);
        return BaseResponse.onSuccess(SuccessStatus.CATEGORY_ADD_SUCCESS, result);
    }

    // 카테고리 조회
    @GetMapping
    public BaseResponse<CategoryDtos.CategoryListDto> getCategory(@AuthenticationPrincipal Long userId) {
        CategoryDtos.CategoryListDto result = categoryService.getCategoryList();
        return BaseResponse.onSuccess(SuccessStatus.CATEGORY_LIST_SUCCESS, result);
    }
}