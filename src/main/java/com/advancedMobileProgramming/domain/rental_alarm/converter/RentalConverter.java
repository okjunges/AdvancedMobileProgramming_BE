package com.advancedMobileProgramming.domain.rental_alarm.converter;

import com.advancedMobileProgramming.domain.rental_alarm.dto.RentalDtos;
import com.advancedMobileProgramming.domain.rental_alarm.entity.RentalDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rental 관련 엔티티 ↔ DTO 변환 담당
 * (현재는 DTO 필드가 정해지지 않아 builder().build()만 호출)
 */
@Component
public class RentalConverter {

    public static RentalDtos.RentalDetailResponseDto toRentalDetailResponseDto(RentalDetail rentalDetail) {

        return RentalDtos.RentalDetailResponseDto.builder()
                // 엔티티의 PK (rental_id) → DTO의 rentalDetailId
                .rentalDetailId(rentalDetail.getRentalId())

                // 연관된 User, Equipment의 PK만 꺼내서 담기
                .userId(rentalDetail.getUser().getId())
                .equipmentId(rentalDetail.getEquipment().getEquipmentId())

                // 날짜 / 상태 정보 그대로 매핑
                .startDate(rentalDetail.getStartDate())
                .returnDate(rentalDetail.getReturnDate())
                .returnStatus(rentalDetail.getReturnStatus())
                .overdue(rentalDetail.getOverdue())
                .build();
    }

    public static RentalDtos.RentalHistoryDto toRentalHistoryDto(RentalDetail rentalDetail) {

        LocalDateTime dueAt = rentalDetail.getStartDate().plusWeeks(1);

        return RentalDtos.RentalHistoryDto.builder()
                .rentalDetailId(rentalDetail.getRentalId())
                .imageUrl(rentalDetail.getEquipment().getImageUrl())
                .name(rentalDetail.getEquipment().getName())
                .modelName(rentalDetail.getEquipment().getModelName())
                .dueAt(dueAt)
                .build();
    }

    public static List<RentalDtos.RentalHistoryDto> toRentalHistoryDtoList(List<RentalDetail> rentalDetails) {
        return rentalDetails.stream()
                .map(RentalConverter::toRentalHistoryDto)
                .toList();
    }

    public static RentalDtos.RentalInfoResponseDto toRentalInfoResponseDto(RentalDetail rentalDetail) {

        // 7일 뒤 마감 예정일
        LocalDateTime dueAt = rentalDetail.getStartDate().plusWeeks(1);

        return RentalDtos.RentalInfoResponseDto.builder()
                .rentalDetailId(rentalDetail.getRentalId())

                // 기자재 정보
                .imageUrl(rentalDetail.getEquipment().getImageUrl())
                .name(rentalDetail.getEquipment().getName())
                .modelName(rentalDetail.getEquipment().getModelName())
                .manufacturer(rentalDetail.getEquipment().getManufacturer())
                .purchaseYear(rentalDetail.getEquipment().getPurchaseYear())
                .use(rentalDetail.getEquipment().getUse())
                .location(rentalDetail.getEquipment().getLocation())

                // 대여/반납 관련
                .startDate(rentalDetail.getStartDate())
                .dueAt(dueAt)
                .returnDate(rentalDetail.getReturnDate())
                .returnStatus(rentalDetail.getReturnStatus())
                .overdue(rentalDetail.getOverdue())
                .build();
    }

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
