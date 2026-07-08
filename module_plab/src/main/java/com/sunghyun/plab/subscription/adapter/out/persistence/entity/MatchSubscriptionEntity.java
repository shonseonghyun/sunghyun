package com.sunghyun.plab.subscription.adapter.out.persistence.entity;


import com.sunghyun.plab.subscription.adapter.out.persistence.converter.NotiSettingConverter;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import jakarta.persistence.*;
import lombok.*;

@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name="match_subscription")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSubscriptionEntity {
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
    @Enumerated(EnumType.STRING)
    private NotiType notiType;

    @Column
    @Convert(converter = NotiSettingConverter.class)
    private NotiSetting notiValue;

    @Column
    private boolean isActive;
}
