package com.sunghyun.batch.dto;

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
    private Integer currentPlayerCnt;
    private ActiveSubType subType;

    // 배치의 흐름 제어를 위한 '상태 플래그' (DB에는 없음)
    private boolean playerCntChanged;
    private boolean subTypeChanged;
}
