package com.advancedMobileProgramming.domain.equipment.application;

import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EquipmentService {
    EquipmentDtos.EquipmentAddResponseDto addEquipment(Long userId, EquipmentDtos.EquipmentAddRequestDto req, MultipartFile mainImage, List<MultipartFile> images) throws IOException;
    void addEquipmentImage(Long userId, String visionCode, List<MultipartFile> images) throws IOException;
}