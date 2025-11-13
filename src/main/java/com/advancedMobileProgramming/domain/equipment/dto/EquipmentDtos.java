package com.advancedMobileProgramming.domain.equipment.dto;

import com.advancedMobileProgramming.domain.category.entity.Category;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class EquipmentDtos {

    // --- 기자재 등록 ---
    // 요청
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentAddRequestDto {
        @NotBlank String visionCode;
        @NotBlank String modelName;
        @NotBlank String name;
        @NotNull Long categoryId;
        @NotBlank String manufacturer;
        @NotNull Integer purchaseYear;
        @NotBlank String location;
        @NotBlank String use;
        @NotNull Integer remainNum;
    }
    // 응답
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentAddResponseDto {
        String visionCode;
        String modelName;
        String name;
        String categoryName;
        String manufacturer;
        Integer purchaseYear;
        String location;
        String use;
        Integer remainNum;
        String imageUrl;
        int rentalCount;
    }

    // --- 기자재 수정 ---
    // 요청
    // 응답

}