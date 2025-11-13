package com.advancedMobileProgramming.domain.equipment.converter;

import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import com.advancedMobileProgramming.domain.equipment.entity.Equipment;

public class EquipmentConverter {

    public static EquipmentDtos.EquipmentAddResponseDto toEquipmentAddResponseDto(Equipment equipment) {
        return EquipmentDtos.EquipmentAddResponseDto.builder()
                .modelName(equipment.getModelName())
                .name(equipment.getName())
                .categoryName(equipment.getCategory().getName())
                .manufacturer(equipment.getManufacturer())
                .purchaseYear(equipment.getPurchaseYear())
                .location(equipment.getLocation())
                .use(equipment.getUse())
                .remainNum(equipment.getRemainNum())
                .imageUrl(equipment.getImageUrl())
                .rentalCount(equipment.getRentalCnt())
                .visionCode(equipment.getVisionCode())
                .build();
    }
}