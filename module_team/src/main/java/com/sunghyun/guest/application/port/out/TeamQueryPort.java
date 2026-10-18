package com.sunghyun.guest.application.port.out;

import com.sunghyun.feign.dto.InternalTeamResponseDto;
import com.sunghyun.web.GlobalResponse;

public interface TeamQueryPort {
    GlobalResponse<InternalTeamResponseDto> getTeamSimpleInfo(Long teamNo);
}
