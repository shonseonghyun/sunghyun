package com.sunghyun.member.application.port.usecase;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;

public interface MemberUseCase {
    MemberResDto getMemberByMemberNo(final Long memberNo);
    MemberResDto registerMember(final MemberRegisterReqDto dto);
    MemberResDto modifyMember(final Long memberNo, final MemberModifyReqDto dto);
    void deleteMember(final Long memberNo);

}
