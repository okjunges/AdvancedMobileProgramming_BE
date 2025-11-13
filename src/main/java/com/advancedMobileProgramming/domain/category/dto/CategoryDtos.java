package com.advancedMobileProgramming.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryDtos {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto {
        @NotBlank String name;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryListDto {
        int count;
        List<CategoryDto> categories;
    }
}