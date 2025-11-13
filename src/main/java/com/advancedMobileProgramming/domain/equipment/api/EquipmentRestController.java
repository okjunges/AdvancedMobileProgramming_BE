package com.advancedMobileProgramming.domain.equipment.api;

import com.advancedMobileProgramming.domain.equipment.application.EquipmentService;
import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import com.advancedMobileProgramming.global.common.code.status.SuccessStatus;
import com.advancedMobileProgramming.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentRestController {
    private final EquipmentService equipmentService;

    // 기자재 등록
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<EquipmentDtos.EquipmentAddResponseDto> addEquipment(@AuthenticationPrincipal Long userId,
                                                                            @RequestPart("metadata") @Valid EquipmentDtos.EquipmentAddRequestDto req,
                                                                            @RequestPart("mainImage") MultipartFile mainImage,
                                                                            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        EquipmentDtos.EquipmentAddResponseDto result = equipmentService.addEquipment(userId, req, mainImage, images);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_ADD_SUCCESS, result);
    }

    // 기자재 사진 데이터 추가
    @PostMapping(value = "/add/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<Void> addEquipmentImage(@AuthenticationPrincipal Long userId, @RequestParam("visionCode") @Valid String visionCode,
                                                @RequestPart("images") List<MultipartFile> images) throws IOException {
        equipmentService.addEquipmentImage(userId, visionCode, images);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_ADD_IMAGE_SUCCESS, null);
    }
}