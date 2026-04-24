package com.sunghyun.plab.subscription.domain.model;

import com.sunghyun.annotation.UpdateAble;
import jakarta.persistence.*;

@Entity
public class PlayerCountSubscription {
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
    @UpdateAble
    private Integer num;
}
