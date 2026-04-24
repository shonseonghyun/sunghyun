package com.sunghyun.plab.subscription.domain.model;

import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import jakarta.persistence.*;

@Entity
public class FreeSubSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionNo;

    @Column
    private Long plabMatchNo;

    @Column
    private Long memberNo;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private ActiveSubType subType;

}
