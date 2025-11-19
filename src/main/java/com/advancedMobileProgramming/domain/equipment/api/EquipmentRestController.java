package com.advancedMobileProgramming.domain.equipment.api;

import com.advancedMobileProgramming.domain.equipment.application.EquipmentService;
import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import com.advancedMobileProgramming.global.common.code.status.SuccessStatus;
import com.advancedMobileProgramming.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public BaseResponse<EquipmentDtos.EquipmentResponseDto> addEquipment(@AuthenticationPrincipal Long userId,
                                                                         @RequestPart("metadata") @Valid EquipmentDtos.EquipmentAddRequestDto req,
                                                                         @RequestPart("mainImage") MultipartFile mainImage,
                                                                         @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        EquipmentDtos.EquipmentResponseDto result = equipmentService.addEquipment(userId, req, mainImage, images);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_ADD_SUCCESS, result);
    }

    // 기자재 사진 데이터 추가
    @PostMapping(value = "/add/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<Void> addEquipmentImage(@AuthenticationPrincipal Long userId, @RequestParam("visionCode") @Valid String visionCode,
                                                @RequestPart("images") List<MultipartFile> images) throws IOException {
        equipmentService.addEquipmentImage(userId, visionCode, images);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_ADD_IMAGE_SUCCESS, null);
    }

    // 기자재 DB 수정
    @PatchMapping("/{equipment_id}")
    public BaseResponse<EquipmentDtos.EquipmentResponseDto> modifyEquipment(@AuthenticationPrincipal Long userId,
                                                                            @PathVariable("equipment_id") Long equipmentId,
                                                                            @RequestBody @Valid EquipmentDtos.EquipmentModifyRequestDto req) {
        EquipmentDtos.EquipmentResponseDto result = equipmentService.modifyEquipment(userId, equipmentId, req);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_MODIFY_SUCCESS, result);
    }

    @DeleteMapping("/{equipment_id}")
    public BaseResponse<Void> deleteEquipment(@AuthenticationPrincipal Long userId,
                                              @PathVariable("equipment_id") Long equipmentId) {
        equipmentService.deleteEquipment(userId, equipmentId);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_DELETE_SUCCESS, null);
    }

    @PostMapping(value ="/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<EquipmentDtos.EquipmentScanResponseDto> scanEquipment(@AuthenticationPrincipal Long userId,
                                                                              @RequestPart(value = "images") MultipartFile image)  throws Exception {
        EquipmentDtos.EquipmentScanResponseDto result = equipmentService.scanWithVertexAi(userId, image);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_SCAN_SUCCESS, result);
    }

    @GetMapping("/popular")
    public BaseResponse<List<EquipmentDtos.PopularEquipmentDto>> getPopularEquipments() {
        List<EquipmentDtos.PopularEquipmentDto> result = equipmentService.getPopularEquipments();
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_POPULAR_SUCCESS, result);
    }

    @GetMapping
    public BaseResponse<List<EquipmentDtos.EquipmentListDto>> getAllEquipments() {
        List<EquipmentDtos.EquipmentListDto> result = equipmentService.getAllEquipments();
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_LIST_SUCCESS, result);
    }

    @GetMapping("/search")
    public BaseResponse<List<EquipmentDtos.EquipmentListDto>> searchEquipments(
            @RequestParam("name") String name
    ) {
        List<EquipmentDtos.EquipmentListDto> result = equipmentService.searchEquipmentsByName(name);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_SEARCH_SUCCESS, result);
    }

    @GetMapping("/filter")
    public BaseResponse<List<EquipmentDtos.EquipmentListDto>> filterEquipments(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) String status
    ) {
        List<EquipmentDtos.EquipmentListDto> result = equipmentService.filterEquipments(categoryId, status);

        SuccessStatus successStatus;

        if (categoryId != null && status != null) {
            successStatus = SuccessStatus.EQUIPMENT_FILTER_SUCCESS;

        } else if (categoryId != null) {
            successStatus = SuccessStatus.EQUIPMENT_CATEGORY_FILTER_SUCCESS;

        } else if (status != null) {
            successStatus = SuccessStatus.EQUIPMENT_RENTAL_STATUS_FILTER_SUCCESS;

        } else {
            successStatus = SuccessStatus.EQUIPMENT_LIST_SUCCESS;
        }

        return BaseResponse.onSuccess(successStatus, result);
    }

    @GetMapping("/{equipment_id}")
    public BaseResponse<EquipmentDtos.EquipmentDetailDto> getEquipmentDetail(
            @PathVariable("equipment_id") Long equipmentId
    ) {
        EquipmentDtos.EquipmentDetailDto result = equipmentService.getEquipmentDetail(equipmentId);
        return BaseResponse.onSuccess(SuccessStatus.EQUIPMENT_DETAIL_SUCCESS, result);
    }
}