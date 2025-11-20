package com.advancedMobileProgramming.domain.equipment.repository;

import com.advancedMobileProgramming.domain.category.entity.Category;
import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByName(String name);

    Optional<Equipment> findByVisionCode(String visionCode);

    Optional<Equipment> findByModelName(String modelName);

    boolean existsEquipmentByName(String name);

    boolean existsEquipmentByVisionCode(String visionCode);

    List<Equipment> findTop4ByOrderByRentalCntDesc();

    List<Equipment> findByNameContainingIgnoreCase(String keyword);

    List<Equipment> findByCategory(Category category);
}