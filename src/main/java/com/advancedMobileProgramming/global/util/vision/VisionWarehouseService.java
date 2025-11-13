package com.advancedMobileProgramming.global.util.vision;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.visionai.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private void uploadOneImage(String visionCode, MultipartFile file) throws IOException {
        // 1) GCS 업로드
        String objectName = "vision/" + visionCode + "/" + UUID.randomUUID()
                + "-" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(props.getBucket(), objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        String gcsUri = "gs://" + props.getBucket() + "/" + objectName;

        // 2) Warehouse REST 호출 준비
        String parent = String.format(
                "projects/%s/locations/%s/corpora/%s",
                props.getProjectNumber(),
                props.getLocation(),
                props.getCorpusId()
        );

        String assetId = visionCode + "-" + UUID.randomUUID();

        String url = String.format(
                "https://warehouse-visionai.googleapis.com/v1/%s/assets?asset_id=%s",
                parent, assetId
        );

        // Cloud Shell에서 쓰던 asset_request.json 과 같은 구조
        Map<String, Object> body = Map.of(
                "assetGcsSource", Map.of(
                        "gcsUri", gcsUri));

        String token = tokenProvider.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    }
}