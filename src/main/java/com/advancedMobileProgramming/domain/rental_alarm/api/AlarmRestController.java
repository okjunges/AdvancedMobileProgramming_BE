package com.advancedMobileProgramming.domain.rental_alarm.api;

import com.advancedMobileProgramming.domain.rental_alarm.application.RentalService;
import com.advancedMobileProgramming.domain.rental_alarm.dto.RentalDtos;
import com.advancedMobileProgramming.global.common.code.status.SuccessStatus;
import com.advancedMobileProgramming.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmRestController {
    private final RentalService rentalService;
    @PostMapping
    public BaseResponse<RentalDtos.RentalAlarmResponseDto> alarm(@AuthenticationPrincipal Long userId) {
        RentalDtos.RentalAlarmResponseDto result = rentalService.getRentalAlarm(userId);
        return BaseResponse.onSuccess(SuccessStatus.ALARM_SUCCESS, result);
    }
}