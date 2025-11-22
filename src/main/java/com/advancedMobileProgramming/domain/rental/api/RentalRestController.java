package com.advancedMobileProgramming.domain.rental.api;

import com.advancedMobileProgramming.domain.rental.application.RentalService;
import com.advancedMobileProgramming.domain.rental.dto.RentalDtos;
import com.advancedMobileProgramming.global.common.code.status.SuccessStatus;
import com.advancedMobileProgramming.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
public class RentalRestController {

    private final RentalService rentalService;

    @PostMapping
    public BaseResponse<RentalDtos.RentalDetailResponseDto> rentEquipment(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RentalDtos.RentalRequestDto req
    ) {
        RentalDtos.RentalDetailResponseDto result = rentalService.rentEquipment(userId, req);
        return BaseResponse.onSuccess(SuccessStatus.RENTAL_CREATE_SUCCESS, result);
    }

    //반납
    @PostMapping("/return")
    public BaseResponse<RentalDtos.RentalDetailResponseDto> returnEquipment(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RentalDtos.ReturnRequestDto req
    ) {
        RentalDtos.RentalDetailResponseDto result = rentalService.returnEquipment(userId, req);
        return BaseResponse.onSuccess(SuccessStatus.RENTAL_RETURN_SUCCESS, result);
    }

    //대여 목록 조회
    @GetMapping("/history")
    public BaseResponse<List<RentalDtos.RentalHistoryDto>> getRentalHistory(
            @AuthenticationPrincipal Long userId
    ) {
        List<RentalDtos.RentalHistoryDto> result =
                rentalService.getRentalHistory(userId);

        return BaseResponse.onSuccess(SuccessStatus.RENTAL_HISTORY_SUCCESS, result);
    }

    //반납 목록 조회
    @GetMapping("/returned")
    public BaseResponse<List<RentalDtos.RentalHistoryDto>> getReturnedHistory(
            @AuthenticationPrincipal Long userId
    ) {
        List<RentalDtos.RentalHistoryDto> result = rentalService.getReturnedHistory(userId);
        return BaseResponse.onSuccess(SuccessStatus.RENTAL_RETURN_LIST_SUCCESS, result);
    }

    //대여 상세 조회
    @GetMapping("/{rental_detail_id}")
    public BaseResponse<RentalDtos.RentalInfoResponseDto> getRentalInfo(
            @AuthenticationPrincipal Long userId,
            @PathVariable("rental_detail_id") Long rentalDetailId
    ) {
        RentalDtos.RentalInfoResponseDto result =
                rentalService.getRentalInfo(userId, rentalDetailId);

        return BaseResponse.onSuccess(SuccessStatus.RENTAL_DETAIL_SUCCESS, result);
    }
}
