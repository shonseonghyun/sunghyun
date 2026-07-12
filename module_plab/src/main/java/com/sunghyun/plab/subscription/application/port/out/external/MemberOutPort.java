package com.sunghyun.plab.subscription.application.port.out.external;

import com.sunghyun.feign.dto.MemberResponseDto;

public interface MemberOutPort {
    MemberResponseDto getMemberResponseDto(final Long memberNo);
}
