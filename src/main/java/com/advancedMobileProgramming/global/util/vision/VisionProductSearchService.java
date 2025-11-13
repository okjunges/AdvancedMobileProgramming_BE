package com.advancedMobileProgramming.global.util.vision;
/*
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

// Google Cloud Vision Product Search 기능으로 구형 -> 현재 유지보수 이슈로 사용 불가능

@Service
@RequiredArgsConstructor
public class VisionProductSearchService {

    private final VisionProperties props;
    private final Storage storage;
    private final ProductSearchClient productSearchClient;

    public void registerEquipmentToVision(String visionCode,
                                          MultipartFile mainImage,
                                          List<MultipartFile> extraImages) throws IOException {

        // 1) Product 생성 (id = visionCode)
        String parent = LocationName.of(props.getProjectId(), props.getLocation()).toString();

        Product product = Product.newBuilder()
                .setDisplayName(visionCode)
                .setProductCategory("general-v1")
                .build();

        CreateProductRequest createReq = CreateProductRequest.newBuilder()
                .setParent(parent)
                .setProduct(product)
                .setProductId(visionCode)
                .build();

        Product created = productSearchClient.createProduct(createReq);

        // 2) ProductSet 에 넣기
        ProductSetName productSetName = ProductSetName.of(
                props.getProjectId(), props.getLocation(), props.getProductSetId());

        AddProductToProductSetRequest addReq = AddProductToProductSetRequest.newBuilder()
                .setName(productSetName.toString())
                .setProduct(created.getName())
                .build();

        productSearchClient.addProductToProductSet(addReq);

        // 3) 이미지들을 GCS에 올리고 ReferenceImage 등록
        uploadAndAttachImage(created.getName(), mainImage);

        if (extraImages != null && extraImages.size() > 0) {
            for (MultipartFile img : extraImages) {
                if (img != null && !img.isEmpty()) {
                    uploadAndAttachImage(created.getName(), img);
                }
            }
        }
    }

    public void appendImagesToProduct(String visionCode, List<MultipartFile> newImages) throws IOException {
        if (newImages == null || newImages.size() == 0) return;

        // 이미 존재하는 Product의 full name 구성
        // projects/{projectId}/locations/{location}/products/{visionCode}
        String productName = ProductName.of(
                props.getProjectId(),
                props.getLocation(),
                visionCode
        ).toString();

        for (MultipartFile img : newImages) {
            if (img != null && !img.isEmpty()) {
                uploadAndAttachImage(productName, img); // 기존 메서드 재사용
            }
        }
    }

    private void uploadAndAttachImage(String productName, MultipartFile file) throws IOException {
        // GCS 경로: vision/{productId}/랜덤-원본파일명
        String objectName = "vision/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(props.getBucket(), objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        String gcsUri = "gs://" + props.getBucket() + "/" + objectName;

        ReferenceImage refImage = ReferenceImage.newBuilder()
                .setUri(gcsUri)
                .build();

        CreateReferenceImageRequest refReq = CreateReferenceImageRequest.newBuilder()
                .setParent(productName) // "projects/.../locations/.../products/..."
                .setReferenceImage(refImage)
                .build();

        productSearchClient.createReferenceImage(refReq);
    }
}*/