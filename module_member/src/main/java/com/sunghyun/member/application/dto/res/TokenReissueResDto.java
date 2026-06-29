package com.sunghyun.member.application.dto.res;

import com.sunghyun.dto.TokenResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenReissueResDto {
    private String id;
    private TokenResDto tokens;
}
