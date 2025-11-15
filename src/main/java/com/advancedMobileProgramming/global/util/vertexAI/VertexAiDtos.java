package com.advancedMobileProgramming.global.util.vertexAI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VertexAiDtos {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecognitionResult {
        String brand;
        String model;
        String category;
        double score;
        String raw; // 혹시 모델이 좀 이상하게 응답했을 때 디버깅용
    }
}