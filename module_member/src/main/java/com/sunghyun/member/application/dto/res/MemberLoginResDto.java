package com.sunghyun.member.application.dto.res;

import com.sunghyun.dto.TokenResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberLoginResDto {
    private Long memberNo;
    private String id;
    private String name;

    private TokenResDto tokens;
}
