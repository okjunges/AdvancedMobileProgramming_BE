package com.advancedMobileProgramming.domain.rental.entity;

import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import com.advancedMobileProgramming.domain.model.BaseEntity;
import com.advancedMobileProgramming.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "rental")
@Getter
@Setter
public class Rental extends BaseEntity {

    // 대여 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 회원PK (user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 기자재PK (equipment_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    // rental_detail 과 1:1 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_detail_id", unique = true, nullable = false)
    private RentalDetail rentalDetail;

    // 알람 여부 (기본 false)
    @Column(name = "alarm", nullable = false)
    private Boolean alarm;

    @Builder
    public Rental(Long id,
                  User user,
                  Equipment equipment,
                  RentalDetail rentalDetail,
                  Boolean alarm) {
        this.id = id;
        this.user = user;
        this.equipment = equipment;
        this.rentalDetail = rentalDetail;
        this.alarm = alarm;
    }

    /**
     * 새로 대여할 때 생성용 팩토리 메서드
     *  - alarm 기본값 false
     */
    public static Rental create(User user,
                                Equipment equipment,
                                RentalDetail rentalDetail) {
        return Rental.builder()
                .user(user)
                .equipment(equipment)
                .rentalDetail(rentalDetail)
                .alarm(false)
                .build();
    }

    public void enableAlarm() {
        this.alarm = true;
    }

    public void disableAlarm() {
        this.alarm = false;
    }
}
