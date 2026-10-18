package com.sunghyun.guest.adapter.out.feign;

import com.sunghyun.feign.TeamExternalOpenFeignClient;
import com.sunghyun.feign.dto.InternalTeamResponseDto;
import com.sunghyun.guest.application.port.out.TeamQueryPort;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamQueryOpenFeignAdapter implements TeamQueryPort {
    private final TeamExternalOpenFeignClient feignClient;

    @Override
    public GlobalResponse<InternalTeamResponseDto> getTeamSimpleInfo(Long teamNo) {
        return feignClient.getTeamSimpleInfo(teamNo);
    }
}
