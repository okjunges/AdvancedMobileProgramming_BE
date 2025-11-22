package com.advancedMobileProgramming.domain.rental_alarm.repository;

import com.advancedMobileProgramming.domain.rental_alarm.entity.Rental;
import com.advancedMobileProgramming.domain.rental_alarm.entity.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental> findByRentalDetail(RentalDetail rentalDetail);
    List<Rental> findAllByUserId(Long userId);
}