package com.advancedMobileProgramming.domain.equipment.converter;

import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import com.advancedMobileProgramming.domain.equipment.entity.Equipment;

import java.util.List;

public class EquipmentConverter {

    public static EquipmentDtos.EquipmentResponseDto toEquipmentAddResponseDto(Equipment equipment) {
        return EquipmentDtos.EquipmentResponseDto.builder()
                .equipmentId(equipment.getEquipmentId())
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

    public static EquipmentDtos.PopularEquipmentDto toPopularEquipmentDto(Equipment equipment) {
        return EquipmentDtos.PopularEquipmentDto.builder()
                .equipmentId(equipment.getEquipmentId())
                .imageUrl(equipment.getImageUrl())
                .modelName(equipment.getModelName())
                .name(equipment.getName())
                .build();
    }

    public static List<EquipmentDtos.PopularEquipmentDto> toPopularEquipmentDtoList(List<Equipment> equipments) {
        return equipments.stream()
                .map(EquipmentConverter::toPopularEquipmentDto)
                .toList();
    }

    public static EquipmentDtos.EquipmentListDto toEquipmentListDto(Equipment equipment) {
        return EquipmentDtos.EquipmentListDto.builder()
                .equipmentId(equipment.getEquipmentId())
                .imageUrl(equipment.getImageUrl())
                .name(equipment.getName())
                .modelName(equipment.getModelName())
                .manufacturer(equipment.getManufacturer())
                .location(equipment.getLocation())
                .build();
    }

    public static List<EquipmentDtos.EquipmentListDto> toEquipmentListDtoList(List<Equipment> equipments) {
        return equipments.stream()
                .map(EquipmentConverter::toEquipmentListDto)
                .toList();
    }

    public static List<EquipmentDtos.EquipmentResponseDto> toEquipmentResponseDtoList(List<Equipment> equipments) {
        return equipments.stream()
                .map(EquipmentConverter::toEquipmentAddResponseDto)
                .toList();
    }

    public static EquipmentDtos.EquipmentDetailDto toEquipmentDetailDto(Equipment equipment) {
        return EquipmentDtos.EquipmentDetailDto.builder()
                .equipmentId(equipment.getEquipmentId())
                .imageUrl(equipment.getImageUrl())
                .name(equipment.getName())
                .modelName(equipment.getModelName())
                .manufacturer(equipment.getManufacturer())
                .purchaseYear(equipment.getPurchaseYear())
                .use(equipment.getUse())
                .location(equipment.getLocation())
                .remainNum(equipment.getRemainNum())
                .available(equipment.getRemainNum() > 0)  // 수량 있으면 true
                .build();
    }
}