package com.advancedMobileProgramming.domain.equipment.entity;

import com.advancedMobileProgramming.domain.category.entity.Category;
import com.advancedMobileProgramming.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Entity
@NoArgsConstructor
@Table(name = "equipment")
@Getter
@Setter
public class Equipment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "model_name", nullable = false, unique = true)
    private String modelName;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "purchase_year", nullable = false)
    private Integer purchaseYear;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "use_detail", nullable = false)
    private String use;

    @Column(name = "remain_num", nullable = false)
    private Integer remainNum;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "rental_cnt")
    private int rentalCnt;

    @Column(name = "vision_code", nullable = false, unique = true)
    private String visionCode;

    @Builder
    public Equipment(Long equipmentId, Category category, String modelName, String name, String manufacturer, Integer purchaseYear,
                     String location, String use, Integer remainNum, String imageUrl, int rentalCnt, String visionCode) {
        this.equipmentId = equipmentId;
        this.category = category;
        this.modelName = modelName;
        this.name = name;
        this.manufacturer = manufacturer;
        this.purchaseYear = purchaseYear;
        this.location = location;
        this.use = use;
        this.remainNum = remainNum;
        this.imageUrl = imageUrl;
        this.rentalCnt = rentalCnt;
        this.visionCode = visionCode;
    }

    public static Equipment addEquipment(Category category, String modelName, String name, String manufacturer, Integer purchaseYear,
                                         String location, String use, Integer remainNum, String imageUrl, String visionCode) {
        return Equipment.builder()
                .category(category)
                .modelName(modelName)
                .name(name)
                .manufacturer(manufacturer)
                .purchaseYear(purchaseYear)
                .location(location)
                .use(use)
                .remainNum(remainNum)
                .imageUrl(imageUrl)
                .rentalCnt(0)
                .visionCode(visionCode)
                .build();
    }
}