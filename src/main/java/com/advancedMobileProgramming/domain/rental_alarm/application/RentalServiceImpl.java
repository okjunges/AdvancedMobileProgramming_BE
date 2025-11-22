package com.advancedMobileProgramming.domain.rental_alarm.application;

import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import com.advancedMobileProgramming.domain.equipment.exception.InsufficientStockException;
import com.advancedMobileProgramming.domain.equipment.exception.NotExistedEquipmentException;
import com.advancedMobileProgramming.domain.equipment.repository.EquipmentRepository;
import com.advancedMobileProgramming.domain.rental_alarm.converter.RentalConverter;
import com.advancedMobileProgramming.domain.rental_alarm.dto.RentalDtos;
import com.advancedMobileProgramming.domain.rental_alarm.entity.Rental;
import com.advancedMobileProgramming.domain.rental_alarm.entity.RentalDetail;
import com.advancedMobileProgramming.domain.rental_alarm.repository.RentalDetailRepository;
import com.advancedMobileProgramming.domain.rental_alarm.repository.RentalRepository;
import com.advancedMobileProgramming.domain.user.entity.User;
import com.advancedMobileProgramming.domain.user.exception.UserNotFoundException;
import com.advancedMobileProgramming.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final RentalDetailRepository rentalDetailRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;


    //유저 조회 공통 메서드
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    //기자재 조회 공통 메서드
    private Equipment getEquipment(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(NotExistedEquipmentException::new);
    }

    //기자재 대여 메서드
    @Override
    @Transactional
    public RentalDtos.RentalDetailResponseDto rentEquipment(Long userId,
                                                            RentalDtos.RentalRequestDto requestDto) {

        User user = getUser(userId);
        Long equipmentId = requestDto.getEquipmentId();

        // 1) 재고 차감 (동시성 제어)
        int updatedRows = equipmentRepository.tryAllocate(equipmentId);
        if (updatedRows == 0) {
            throw new InsufficientStockException();
        }

        // 2) 기자재 조회
        Equipment equipment = getEquipment(equipmentId);

        // 3) rental_detail 생성 & 저장
        RentalDetail rentalDetail = RentalDetail.create(
                user,
                equipment,
                LocalDateTime.now()
        );
        rentalDetailRepository.save(rentalDetail);

        // 4) rental 생성 & 저장 (rentalDetail 연결!)
        Rental rental = Rental.create(user, equipment, rentalDetail);
        rentalRepository.save(rental);

        // 5) 응답 DTO
        return RentalConverter.toRentalDetailResponseDto(rentalDetail);
    }

    //기자재 반납 메서드
    @Override
    @Transactional
    public RentalDtos.RentalDetailResponseDto returnEquipment(Long userId,
                                                              RentalDtos.ReturnRequestDto requestDto) {

        // 1) 유저 확인
        User user = getUser(userId);

        // 2) rental_detail 조회
        Long rentalDetailId = requestDto.getRentalDetailId();
        RentalDetail rentalDetail = rentalDetailRepository.findById(rentalDetailId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이용내역입니다."));

        // 3) 본인 이용내역인지 체크
        if (!rentalDetail.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 이용내역만 반납할 수 있습니다.");
        }

        // 4) 이미 반납됐는지 체크
        if (Boolean.TRUE.equals(rentalDetail.getReturnStatus())) {
            throw new IllegalStateException("이미 반납된 이용내역입니다.");
        }

        // 5) 연체 여부 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = rentalDetail.getStartDate().plusWeeks(1); // 7일 뒤
        boolean overdue = now.isAfter(dueDate);

        // 6) rental_detail 상태 업데이트
        rentalDetail.completeReturn(now, overdue);

        // 7) rental 한 건만 삭제 (id 기반 1:1)
        rentalRepository.findByRentalDetail(rentalDetail)
                .ifPresent(rentalRepository::delete);

        // 8) 기자재 재고 +1
        Equipment equipment = rentalDetail.getEquipment();
        equipmentRepository.releaseOne(equipment.getEquipmentId());

        // 9) 응답 DTO
        return RentalConverter.toRentalDetailResponseDto(rentalDetail);
    }

    //기자재 대여 목록 조회 메서드
    @Override
    @Transactional(readOnly = true)
    public List<RentalDtos.RentalHistoryDto> getRentalHistory(Long userId) {

        User user = getUser(userId);

        List<RentalDetail> details =
                rentalDetailRepository.findByUserIdAndReturnStatusFalseOrderByStartDateDesc(user.getId());

        return RentalConverter.toRentalHistoryDtoList(details);
    }

    //기자재 반납 목록 조회 메서드
    @Override
    @Transactional(readOnly = true)
    public List<RentalDtos.RentalHistoryDto> getReturnedHistory(Long userId) {

        // (선택) 유저 검증
        User user = getUser(userId);

        List<RentalDetail> details =
                rentalDetailRepository.findByUserIdAndReturnStatusTrueOrderByStartDateDesc(user.getId());

        return RentalConverter.toRentalHistoryDtoList(details);
    }

    //기자재 상세 정보 메서드
    @Override
    @Transactional(readOnly = true)
    public RentalDtos.RentalInfoResponseDto getRentalInfo(Long userId,
                                                          Long rentalDetailId) {

        // 1) 로그인 유저 확인
        User user = getUser(userId);

        // 2) rental_detail 조회
        RentalDetail rentalDetail = rentalDetailRepository.findById(rentalDetailId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 내역입니다."));

        // 3) 본인 건인지 체크 (보안)
        if (!rentalDetail.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 대여 내역만 조회할 수 있습니다.");
        }

        // 4) DTO 변환
        return RentalConverter.toRentalInfoResponseDto(rentalDetail);
    }

    @Override
    public RentalDtos.RentalAlarmResponseDto getRentalAlarm(Long userId) {
        User user = getUser(userId);
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);
        List<RentalDetail> alarmList = new ArrayList<>();
        for (Rental rental : rentals) {
            RentalDetail rentalDetail = rental.getRentalDetail();
            // 로컬로만 진행할 프로젝트여서 OS가 한국 시간 기준이어서 지역 시간 문제 없음
            LocalDate dueTo = rentalDetail.getStartDate().toLocalDate();
            if (dueTo.plusWeeks(1).isEqual(LocalDate.now()) && !rental.getAlarm()) {
                alarmList.add(rentalDetail);
                rental.setAlarm(true);
            }
        }
        return RentalConverter.toRentalAlarmResponseDto(alarmList);
    }
}
