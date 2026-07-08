package com.sunghyun.batch.dto;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.PlabMatchData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTargetDto implements PlabMatchData {
    // 1. 구독 정보 (대상자)
    private Long subscriptionNo;
    private Long plabMatchNo;
    private Long memberNo;
    private String email;

    private String stadiumName;
    private String matchDt;
    private String matchTm;
    private Integer maxPlayerCnt;

    // 2. 알림 조건
    private NotiType notiType;    // 구독한 알림 타입 (예: PLAYER_COUNT)
    private NotiSetting notiValue;   // 구독한 설정값 (예: 7명에 해당하는 code)

    // 3. JOIN으로 가져올 현재 매치의 실시간 정보
    private NotiSetting playerCnt;
    private NotiSetting subType;
}

