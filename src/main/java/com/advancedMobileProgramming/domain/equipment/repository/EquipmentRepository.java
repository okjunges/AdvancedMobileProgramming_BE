package com.advancedMobileProgramming.domain.equipment.repository;

import com.advancedMobileProgramming.domain.category.entity.Category;
import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Equipment e
           SET e.remainNum = e.remainNum - 1,
               e.rentalCnt = e.rentalCnt + 1
         WHERE e.equipmentId = :equipmentId
           AND e.remainNum >= 1
    """)
    int tryAllocate(@Param("equipmentId") Long equipmentId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE Equipment e
       SET e.remainNum = e.remainNum + 1
     WHERE e.equipmentId = :equipmentId
""")
    int releaseOne(@Param("equipmentId") Long equipmentId);
}