package com.sunghyun.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTargetDto {
    // 1. 구독 정보 (대상자)
    private Long subscriptionNo;
    private Long plabMatchNo;
    private Long memberNo;
    private String email;

    private String stadiumName;
    private String matchDt;
    private String matchTm;

    // 2. 알림 조건
    private String notiType;    // 구독한 알림 타입 (예: PLAYER_COUNT)
    private String notiValue;   // 구독한 설정값 (예: 7명에 해당하는 code)

    // 3. JOIN으로 가져올 현재 매치의 실시간 정보
    private String currentPlayerCnt;
    private String currentSubType;

//    public boolean isEqualsPlayerCnt(){
//        return this.currentPlayerCnt.equals(targetPlayerCnt)
//                && !(this.currentPlayerCnt.equals(0))
//                ;
//    }
//
//    public boolean isEqualsSubType(){
//        return this.currentSubType.equals(subType)
//                && !(this.currentSubType.equals(ActiveSubType.NONE))
//                ;
//    }

}

