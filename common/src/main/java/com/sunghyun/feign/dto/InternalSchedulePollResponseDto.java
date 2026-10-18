package com.sunghyun.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InternalSchedulePollResponseDto {
    private Long teamNo; // 팀 API를 통한 팀정보 가져오기 위한 teamNo

    private String matchType;
    private InternalLocationResponseDto location;
    private String playDt;
    private String playTm;
}
