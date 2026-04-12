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
    // 1. 구독 정보 (기본)
    private Long subscriptionNo;
    private Long plabMatchNo;
    private Long memberNo;
    private String email;
    private Integer targetPlayerCnt;
    private ActiveSubType subType;
    private boolean isNotified;

    // 2. JOIN으로 가져올 현재 매치의 실시간 정보
    private Integer currentPlayerCnt; // DB의 plab_match.current_player_cnt
    private ActiveSubType currentSubType; // DB의 plab_match.sub_type

    public boolean isEqualsPlayerCnt(){
        return this.currentPlayerCnt.equals(targetPlayerCnt)
                && !(this.currentPlayerCnt.equals(0))
                ;
    }

    public boolean isEqualsSubType(){
        return this.currentSubType.equals(subType)
                && !(this.currentSubType.equals(ActiveSubType.NONE))
                ;
    }

}

