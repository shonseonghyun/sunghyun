package com.sunghyun.config;

import com.sunghyun.dto.TokenResponseDto;
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

    private TokenResponseDto tokens;
}
