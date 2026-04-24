package com.sunghyun.plab.history.adapter.out.persistence;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import jakarta.persistence.*;

@Table(name = "noti_history")
@Entity
public class NotiHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    private Long subscriptionNo;

    private String memberNo;

    private String email;

    private NotiType notiType;

    private NotiSetting notiSetting;

    private String sendDt;

    private String sendTm;
}
