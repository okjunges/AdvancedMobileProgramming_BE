package com.advancedMobileProgramming.domain.rental.application;

import com.advancedMobileProgramming.domain.rental.dto.RentalDtos;

import java.util.List;

public interface RentalService {

    RentalDtos.RentalDetailResponseDto rentEquipment(Long userId,
                                                     RentalDtos.RentalRequestDto requestDto);

    //반납 처리
    RentalDtos.RentalDetailResponseDto returnEquipment(Long userId,
                                                       RentalDtos.ReturnRequestDto requestDto);

    List<RentalDtos.RentalHistoryDto> getRentalHistory(Long userId);

    List<RentalDtos.RentalHistoryDto> getReturnedHistory(Long userId);

    RentalDtos.RentalInfoResponseDto getRentalInfo(Long userId,
                                                   Long rentalDetailId);
}
