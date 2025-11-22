package com.advancedMobileProgramming.domain.rental.repository;

import com.advancedMobileProgramming.domain.rental.entity.Rental;
import com.advancedMobileProgramming.domain.rental.entity.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental> findByRentalDetail(RentalDetail rentalDetail);

}