package com.advancedMobileProgramming.domain.rental.entity;

import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import com.advancedMobileProgramming.domain.model.BaseEntity;
import com.advancedMobileProgramming.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "rental_detail")
@Getter
@Setter
public class RentalDetail extends BaseEntity {

    // 이용내역PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id", nullable = false)
    private Long rentalId;

    // 회원PK (user_id2)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id2", nullable = false)
    private User user;

    // 기자재PK (equipment_id2)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id2", nullable = false)
    private Equipment equipment;

    // 시작일 (NOT NULL)
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /*
    // 대여분류 (enum) – 실제 DB는 enum이지만, 도메인 enum 정해지기 전이라 String으로 매핑
    @Column(name = "type", nullable = false)
    private String type;
    */

    // 반납일시 (NULL 허용)
    @Column(name = "return_date")
    private LocalDateTime returnDate;

    // 반납상태 (bool, 기본 false)
    @Column(name = "return_status")
    private Boolean returnStatus;

    // 연체여부 (bool)
    @Column(name = "overdue")
    private Boolean overdue;

    @Builder
    public RentalDetail(Long rentalId,
                        User user,
                        Equipment equipment,
                        LocalDateTime startDate,
                        //String type,
                        LocalDateTime returnDate,
                        Boolean returnStatus,
                        Boolean overdue) {
        this.rentalId = rentalId;
        this.user = user;
        this.equipment = equipment;
        this.startDate = startDate;
        //this.type = type;
        this.returnDate = returnDate;
        this.returnStatus = returnStatus;
        this.overdue = overdue;
    }

    /**
     * 새 대여 이용내역 생성용 팩토리 메서드
     *  - returnStatus 기본 false
     *  - overdue 기본 false
     */
    public static RentalDetail create(User user,
                                      Equipment equipment,
                                      LocalDateTime startDate) {
        return RentalDetail.builder()
                .user(user)
                .equipment(equipment)
                .startDate(startDate)
                .returnDate(null)      // 또는 아예 빌더에서 생략
                .returnStatus(false)
                .overdue(false)
                .build();
    }

    // 실제 반납할 때 호출
    public void completeReturn(LocalDateTime returnDate, boolean overdue) {
        this.returnDate = returnDate;   // 실제 반납시각
        this.returnStatus = true;
        this.overdue = overdue;
    }
}
