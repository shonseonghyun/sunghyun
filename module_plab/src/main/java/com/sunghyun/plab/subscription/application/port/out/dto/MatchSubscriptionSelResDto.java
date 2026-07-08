package com.sunghyun.plab.subscription.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MatchSubscriptionSelResDto {
    // 🟢 [MatchSubscription] 관련 필드
    private Long subscriptionNo;
    private Long memberNo;
    private String email;
    private NotiType notiType;
    private String notiValue;

    @JsonProperty("isActive")
    private boolean isActive;

    // 🟢 공통 외래키 필드 (한 번만 선언)
    private Long plabMatchNo;

    // 🟢 [PlabMatch] 관련 필드
    private String stadiumName;
    private Integer stadiumNo;
    private String matchDt;
    private String matchTm;
    private String playerCnt;
    private Integer maxPlayerCnt;
    private NotiSetting subType;
    private MatchStatus status;
}
