package com.advancedMobileProgramming.domain.rental.repository;

import com.advancedMobileProgramming.domain.rental.entity.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalDetailRepository extends JpaRepository<RentalDetail, Long> {

    List<RentalDetail> findByUserIdAndReturnStatusFalseOrderByStartDateDesc(Long userId);
    List<RentalDetail> findByUserIdAndReturnStatusTrueOrderByStartDateDesc(Long userId);
}