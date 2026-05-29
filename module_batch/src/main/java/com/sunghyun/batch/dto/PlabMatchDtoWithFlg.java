package com.sunghyun.batch.dto;

import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlabMatchDtoWithFlg {
    private Long matchNo;
    private Long plabMatchNo;
    private String stadiumName;
    private Integer stadiumNo;
    private String matchDt;
    private String matchTm;
    private NotiSetting playerCnt;
    private NotiSetting subType;
    private MatchStatus status;

    // 배치의 흐름 제어를 위한 '상태 플래그' (DB에는 없음)
    private boolean playerCntChanged;
    private boolean subTypeChanged;
}
