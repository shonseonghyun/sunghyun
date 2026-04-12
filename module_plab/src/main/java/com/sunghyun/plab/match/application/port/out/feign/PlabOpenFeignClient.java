package com.sunghyun.plab.match.application.port.out.feign;

import com.sunghyun.feign.dto.PlabMatchResponseDto;

public interface PlabOpenFeignClient {
    PlabMatchResponseDto getMatch(Long plabMatchNo);
}
