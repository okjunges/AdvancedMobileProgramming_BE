package com.advancedMobileProgramming.domain.rental_alarm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 대여(rental) 관련 DTO 모음 클래스
 */
public class RentalDtos {

    //기자재 대여 요청 Dto
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RentalRequestDto {
        @NotNull
        private Long equipmentId;
    }

    //기자재 대여 응답 Dto
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RentalDetailResponseDto {

        private Long rentalDetailId;
        private Long userId;
        private Long equipmentId;
        private LocalDate startDate;
        private LocalDate returnDate;   // 대여 직후에는 보통 null
        private Boolean returnStatus;       // 대여 직후에는 false
        private Boolean overdue;            // 대여 직후에는 false
    }

    //반납 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReturnRequestDto {

        @NotNull
        private Long rentalDetailId;
    }

    //기자재 목록 객체 Dto
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RentalHistoryDto {

        // rental_detail PK
        private Long rentalDetailId;

        // 기자재 카드에 필요한 정보
        private String imageUrl;    // equipment.imageUrl
        private String name;        // equipment.name
        private String modelName;   // equipment.modelName
        private Boolean overDue;

        // 반납 예정일 (start_date + 7일)
        private LocalDate dueAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RentalInfoResponseDto {

        // 식별용
        private Long rentalDetailId;

        // 화면에 보여줄 기자재 정보
        private String imageUrl;
        private String name;
        private String modelName;
        private String manufacturer;
        private Integer purchaseYear;
        private String use;
        private String location;

        // 대여/반납 관련
        private LocalDate startDate;   // 시작일
        private LocalDate dueAt;       // 마감 예정일 (startDate + 7일)
        private LocalDate returnDate;  // 실제 반납 시각 (반납 전이면 null)
        private Boolean returnStatus;      // 반납 여부
        private Boolean overdue;           // 연체 여부
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Alarm{
        private String name;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RentalAlarmResponseDto {
        private int num;
        private List<Alarm> alarms;
    }
}
