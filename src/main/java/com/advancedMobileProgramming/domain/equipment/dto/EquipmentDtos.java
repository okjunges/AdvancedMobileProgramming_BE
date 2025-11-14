package com.advancedMobileProgramming.domain.equipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EquipmentDtos {

    // --- 기자재 응답 ---
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentResponseDto {
        Long equipmentId;
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

    // --- 기자재 등록 요청 ---
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

    // --- 기자재 수정 요청 ---
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentModifyRequestDto {
        String modelName;
        String name;
        Long categoryId;
        String manufacturer;
        Integer purchaseYear;
        String location;
        String use;
        Integer remainNum;
    }

    // --- 기자재 스캔 응답 ---
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentScanResponseDto {
        Double score;
        EquipmentResponseDto equipment;
    }

}