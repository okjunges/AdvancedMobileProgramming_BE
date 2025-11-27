package com.advancedMobileProgramming.domain.rental_alarm.converter;

import com.advancedMobileProgramming.domain.rental_alarm.dto.RentalDtos;
import com.advancedMobileProgramming.domain.rental_alarm.entity.RentalDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RentalConverter {

    // 대여 생성/반납 공통 응답 DTO
    public static RentalDtos.RentalDetailResponseDto toRentalDetailResponseDto(RentalDetail rentalDetail) {

        return RentalDtos.RentalDetailResponseDto.builder()
                .rentalDetailId(rentalDetail.getRentalId())
                .userId(rentalDetail.getUser().getId())
                .equipmentId(rentalDetail.getEquipment().getEquipmentId())
                .startDate(rentalDetail.getStartDate())      // LocalDate 그대로
                .returnDate(rentalDetail.getReturnDate())    // LocalDate 그대로 (null 가능)
                .returnStatus(rentalDetail.getReturnStatus())
                .overdue(rentalDetail.getOverdue())
                .build();
    }

    // “대여 중 / 반납 목록” 카드용 DTO
    public static RentalDtos.RentalHistoryDto toRentalHistoryDto(RentalDetail rentalDetail) {

        // startDate 가 이미 LocalDate 이므로 toLocalDate() 필요 없음
        LocalDate dueAt = rentalDetail.getStartDate().plusWeeks(1);

        Boolean overDue = dueAt.isBefore(LocalDate.now());

        return RentalDtos.RentalHistoryDto.builder()
                .rentalDetailId(rentalDetail.getRentalId())
                .imageUrl(rentalDetail.getEquipment().getImageUrl())
                .name(rentalDetail.getEquipment().getName())
                .modelName(rentalDetail.getEquipment().getModelName())
                .overDue(overDue)
                .dueAt(dueAt)
                .build();
    }

    public static List<RentalDtos.RentalHistoryDto> toRentalHistoryDtoList(List<RentalDetail> rentalDetails) {
        return rentalDetails.stream()
                .map(RentalConverter::toRentalHistoryDto)
                .toList();
    }

    // 대여 상세 조회 DTO
    public static RentalDtos.RentalInfoResponseDto toRentalInfoResponseDto(RentalDetail rentalDetail) {

        LocalDate dueAt = rentalDetail.getStartDate().plusWeeks(1);

        return RentalDtos.RentalInfoResponseDto.builder()
                .rentalDetailId(rentalDetail.getRentalId())

                .imageUrl(rentalDetail.getEquipment().getImageUrl())
                .name(rentalDetail.getEquipment().getName())
                .modelName(rentalDetail.getEquipment().getModelName())
                .manufacturer(rentalDetail.getEquipment().getManufacturer())
                .purchaseYear(rentalDetail.getEquipment().getPurchaseYear())
                .use(rentalDetail.getEquipment().getUse())
                .location(rentalDetail.getEquipment().getLocation())

                .startDate(rentalDetail.getStartDate())       // LocalDate
                .dueAt(dueAt)                                 // LocalDate
                .returnDate(rentalDetail.getReturnDate())     // LocalDate (null 가능)
                .returnStatus(rentalDetail.getReturnStatus())
                .overdue(rentalDetail.getOverdue())
                .build();
    }

    // 알림용 DTO
    public static RentalDtos.Alarm toRentalAlarmDto(RentalDetail rentalDetail) {
        return RentalDtos.Alarm.builder()
                .name(rentalDetail.getEquipment().getModelName())
                .build();
    }

    public static RentalDtos.RentalAlarmResponseDto toRentalAlarmResponseDto(List<RentalDetail> rentalDetails) {
        List<RentalDtos.Alarm> alarms = rentalDetails.stream()
                .map(RentalConverter::toRentalAlarmDto)
                .toList();

        return RentalDtos.RentalAlarmResponseDto.builder()
                .num(rentalDetails.size())
                .alarms(alarms)
                .build();
    }
}
