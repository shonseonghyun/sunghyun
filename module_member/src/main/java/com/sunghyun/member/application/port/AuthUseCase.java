package com.sunghyun.member.application.port;

import com.sunghyun.member.application.dto.req.TokenReissueReqDto;
import com.sunghyun.member.application.dto.res.MemberLoginResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.dto.res.TokenReissueResDto;

public interface AuthUseCase {
    MemberValidIdResDto validMemberId(final String id);
    MemberLoginResDto login(final String id, final String inputRawPwd);
    TokenReissueResDto reissueAccessToken(final TokenReissueReqDto tokenReissueReqDto);
}
