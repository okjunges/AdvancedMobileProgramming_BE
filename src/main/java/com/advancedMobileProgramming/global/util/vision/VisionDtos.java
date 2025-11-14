package com.advancedMobileProgramming.global.util.vision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VisionDtos {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScanDto {
        double relevance;
        String visionCode;
    }
}