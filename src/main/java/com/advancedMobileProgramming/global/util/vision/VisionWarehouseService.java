package com.advancedMobileProgramming.global.util.vision;

import com.advancedMobileProgramming.domain.equipment.exception.NotMatchingEquipmentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisionWarehouseService {

    private final VisionProperties props;
    private final Storage storage;  // GCS
    private final GcpTokenProvider tokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registerEquipmentToVision(String visionCode,
                                          MultipartFile mainImage,
                                          List<MultipartFile> extraImages) throws IOException {
        if (mainImage == null || mainImage.isEmpty()) {
            throw new IllegalArgumentException("mainImage is required");
        }
        if (extraImages == null) extraImages = List.of();

        uploadOneImage(visionCode, mainImage);

        for (MultipartFile img : extraImages) {
            if (img != null && !img.isEmpty()) {
                uploadOneImage(visionCode, img);
            }
        }
    }

    public void appendImagesToProduct(String visionCode,
                                      List<MultipartFile> newImages) throws IOException {
        if (newImages == null || newImages.isEmpty()) return;

        for (MultipartFile img : newImages) {
            if (img != null && !img.isEmpty()) {
                uploadOneImage(visionCode, img);
            }
        }
    }

    private void uploadOneImage(String visionCode, MultipartFile image) throws IOException {
        // 1) GCS 업로드
        String objectName = "vision/" + visionCode + "/" + UUID.randomUUID()
                + "-" + image.getOriginalFilename();

        BlobId blobId = BlobId.of(props.getBucket(), objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(image.getContentType())
                .build();

        storage.create(blobInfo, image.getBytes());

        String gcsUri = "gs://" + props.getBucket() + "/" + objectName;

        // 2) Warehouse REST 호출 준비
        // 공식 문서 : https://docs.cloud.google.com/vision-ai/docs/reference/rest/v1/projects.locations.corpora.assets/upload?utm_source=chatgpt.com
        String parent = String.format("projects/%s/locations/%s/corpora/%s",
                props.getProjectNumber(), props.getLocation(), props.getCorpusId());

        String assetId = visionCode + "-" + UUID.randomUUID();

        String url = String.format(
                "https://warehouse-visionai.googleapis.com/v1/%s/assets?asset_id=%s",
                parent, assetId
        );

        // Cloud Shell에서 쓰던 asset_request.json 과 같은 구조
        Map<String, Object> body = Map.of(
                "assetGcsSource", Map.of(
                        "gcsUri", gcsUri));

        String json = callRest(body, url);
        System.out.println("등록 응답 : " + json);
    }

    public VisionDtos.ScanDto scan(MultipartFile image) throws IOException {
        // 공식 문서 : https://docs.cloud.google.com/vision-ai/docs/image-warehouse-search?hl=ko
        byte[] bytes = image.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes); // :searchIndexEndpoint
        String url = String.format("https://warehouse-visionai.googleapis.com/v1/projects/%s/locations/%s/indexEndpoints/%s:searchIndexEndpoint",
                props.getProjectNumber(), props.getLocation(), props.getIndexEndpointId());
        Map<String, Object> body = Map.of(
                "imageQuery", Map.of(
                        "inputImage", base64));

        String json = callRest(body, url);

        Map<String, Object> root = objectMapper.readValue(json, Map.class);
        List<Map<String, Object>> items = (List<Map<String, Object>>) root.get("searchResultItems");

        if (items == null || items.isEmpty()) throw new NotMatchingEquipmentException();

        // 0번째 = relevance 가장 높은 결과
        Map<String, Object> best = items.get(0);

        String assetName = (String) best.get("asset");
        double relevance = ((Number) best.get("relevance")).doubleValue();

        System.out.println("relevance: " + relevance);

        if (relevance < 0.8) throw new NotMatchingEquipmentException();

        String assetId = assetName.substring(assetName.lastIndexOf("/") + 1);
        String[] parts = assetId.split("-");
        String visionCode = parts[0] + "-" + parts[1];

        return VisionDtos.ScanDto.builder()
                .relevance(relevance)
                .visionCode(visionCode)
                .build();
    }

    private String callRest(Map<String, Object> body, String url) throws IOException  {
        String token = tokenProvider.getAccessToken();

        System.out.println("Req Body : " + new ObjectMapper().writeValueAsString(body));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // REST 호출
        ResponseEntity<String> resp = restTemplate.postForEntity(url, entity, String.class);
        String json = resp.getBody();
        System.out.println("=== Vision call ===");
        System.out.println("URL       : " + url);
        System.out.println("HTTP Code : " + resp.getStatusCode());
        System.out.println("Resp Body : " + resp.getBody());

        return json;
    }
}