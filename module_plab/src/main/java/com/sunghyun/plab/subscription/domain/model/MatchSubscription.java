package com.sunghyun.plab.subscription.domain.model;


import com.sunghyun.annotation.UpdateAble;
import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import jakarta.persistence.*;
import lombok.*;

@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name="match_subscription")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionNo;

    @Column
//    @OneToMany //두 개의 생명주기가 다르기에 일단 분리할 생각으로 연관매핑하지 않았다.
    private Long plabMatchNo;

    @Column
    private Long memberNo;

    @Column
    private String email;

    @Column
    @UpdateAble //null이면 업데이트 안 함
    private Integer targetPlayerCnt;

    @Column
    @UpdateAble //null이면 업데이트 안 함
    private ActiveSubType subType;

    @Column
    private boolean isNotified;

    public void resetNotification(){
        setNotified(false);
    }

    public static MatchSubscription create(
            final Long plabMatchNo,
            final Long memberNo,
            final String email,
            final Integer targetPlayerCnt,
            final ActiveSubType subType
    ) {
        // 유효성 검증 (필수)
        // 파라미터
//        if (plabMatchNo == null || memberNo == null) {
//            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
//        }

        return MatchSubscription.builder()
                .plabMatchNo(plabMatchNo)
                .memberNo(memberNo)
                .email(email)
                .targetPlayerCnt(targetPlayerCnt)
                .subType(subType)
                .isNotified(false) // 초기 상태 강제
                .build();
    }
}
