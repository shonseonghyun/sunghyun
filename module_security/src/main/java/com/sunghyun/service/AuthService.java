package com.sunghyun.service;

import com.sunghyun.config.MemberLoginResDto;
import com.sunghyun.dto.TokenReissueReqDto;

public interface AuthService {
    MemberLoginResDto authenticate(final String id, final String pwd);
    void reissueAccessToken(final TokenReissueReqDto tokenReissueReqDto);
}
