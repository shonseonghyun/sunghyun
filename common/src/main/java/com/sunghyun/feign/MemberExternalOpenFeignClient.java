package com.sunghyun.feign;

import com.sunghyun.feign.dto.MemberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "member-external-api",
        url = "${member.api.url}",
        configuration = {OpenFeignConfig.class,InternalOpenFeignConfig.class}
)
public interface MemberExternalOpenFeignClient {
    @GetMapping("/{memberNo}")
    MemberResponseDto getMember(@PathVariable final Long memberNo);
}
