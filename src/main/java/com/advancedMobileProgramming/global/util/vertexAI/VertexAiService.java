package com.advancedMobileProgramming.global.util.vertexAI;

import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import com.advancedMobileProgramming.domain.equipment.repository.EquipmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VertexAiService {
    private final EquipmentRepository equipmentRepository;
    private final @Qualifier("vertexRestTemplate") RestTemplate vertexRestTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${vertex-ai.project-id}")
    private String projectId;

    @Value("${vertex-ai.location}")
    private String location;

    @Value("${vertex-ai.model}")
    private String model;

    public VertexAiDtos.RecognitionResult recognize(MultipartFile image) throws Exception {

        // 1) Base64 인코딩
        byte[] bytes = image.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);

        // 2) 요청 URL
        String url = String.format(
                "https://%s-aiplatform.googleapis.com/v1/projects/%s/locations/%s/publishers/google/models/%s:generateContent",
                location, projectId, location, model);

        // 3) DB에 있는 모든 기자재 반환
        List<Equipment> list = equipmentRepository.findAll();

        // 4) Prompt 만들기
        StringBuilder promptList = new StringBuilder("기자재 예시) 브랜드 - 모델명 - 카테고리\n");
        for (Equipment equipment : list) {
            promptList.append(equipment.getManufacturer() + " - " + equipment.getModelName() + " - " + equipment.getCategory().getName() + "\n");
        }
        String prompt = String.format("""
        이 이미지를 분석해서 다음 기자재 목록 중에서 가장 일치한 기자재와 일치 정도를 0~100으로 점수화해서 알려줘.
        만약 일치하는 기자재가 없다면 일치 정도를 0으로 반환하면 돼

        %s
        
        절대로 백틱(```)이나 설명을 출력하지 마. 아래 JSON만 정확히 출력해:
        반드시 아래 JSON 형태로 정확히 답변하고, 내가 준 목록에 있는 기자재 정보 그대로 반환해야 돼
        {
          "brand": "...",
          "model": "...",
          "category": "...",
          "score": 0~100 사이의 숫자 (문자열이 아님)
        }
        """, promptList.toString());


        // 4) 멀티모달 요청 JSON (텍스트 + 이미지)
        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "role", "user",
                                "parts", new Object[]{
                                        Map.of("text", prompt),
                                        Map.of(
                                                "inlineData",
                                                Map.of(
                                                        "mimeType", image.getContentType(),
                                                        "data", base64
                                                )
                                        )
                                }
                        )
                }
        );

        // 4) Vertex AI로 POST 호출
        String responseJson = vertexRestTemplate.postForObject(url, body, String.class);

        // 5) 응답 JSON 파싱
        JsonNode root = objectMapper.readTree(responseJson);
        String text = root
                .path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();

        // 6) 멀티모달 모델이 반환한 JSON 텍스트 파싱
        JsonNode resultNode = objectMapper.readTree(text);

        System.out.println("===== EXTRACTED TEXT =====");
        System.out.println(text);

        return VertexAiDtos.RecognitionResult.builder()
                .brand(resultNode.path("brand").asText())
                .model(resultNode.path("model").asText())
                .category(resultNode.path("category").asText())
                .score(resultNode.path("score").asDouble(0.0))
                .raw(text)
                .build();
    }
}