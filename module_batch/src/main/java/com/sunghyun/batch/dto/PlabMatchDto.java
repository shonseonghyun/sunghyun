package com.sunghyun.batch.dto;

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
    private Integer currentPlayerCnt;
    private ActiveSubType subType;
}
