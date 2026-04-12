package com.sunghyun.member.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberValidIdResDto {
    private String pendingToken;

    /** 토큰 유효 시간 (단위: 분) */
    private Long timeout;
}
