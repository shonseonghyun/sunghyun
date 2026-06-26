package com.sunghyun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenResDto {
    private String accessToken;
    private String refreshToken;
}
