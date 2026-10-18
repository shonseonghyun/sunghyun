package com.sunghyun.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InternalTeamResponseDto {
    private Long teamNo;
    private Long creatorMemberNo;
    private String teamName;                     // 팀명
    private String logoUrl;                      // 로고(선택)
    private String teamDesc;
}
