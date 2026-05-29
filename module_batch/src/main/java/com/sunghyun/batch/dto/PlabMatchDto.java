package com.sunghyun.batch.dto;

import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlabMatchDto {
    private Long matchNo;
    private Long plabMatchNo;
    private String stadiumName;
    private Integer stadiumNo;
    private String matchDt;
    private String matchTm;
    private NotiSetting playerCnt;
    private NotiSetting subType;
    private MatchStatus status;
}