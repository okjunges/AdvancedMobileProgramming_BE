package com.advancedMobileProgramming.domain.equipment.repository;

import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByName(String name);

    boolean existsEquipmentByName(String name);

    boolean existsEquipmentByVisionCode(String visionCode);
}