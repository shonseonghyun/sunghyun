package com.sunghyun.member.application.dto.req;

import lombok.Getter;

@Getter
public class TokenReissueReqDto {
    private String id;
    private String refreshToken;
}
