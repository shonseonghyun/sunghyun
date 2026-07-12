package com.sunghyun.plab.subscription.adapter.out.external.feign;

import com.sunghyun.feign.MemberExternalOpenFeignClient;
import com.sunghyun.feign.dto.MemberResponseDto;
import com.sunghyun.plab.subscription.application.port.out.external.MemberOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberOpenFeignClientAdapter implements MemberOutPort {
    private final MemberExternalOpenFeignClient memberExternalOpenFeignClient;

    @Override
    public MemberResponseDto getMemberResponseDto(final Long memberNo) {
        return memberExternalOpenFeignClient.getMember(memberNo);
    }
}
