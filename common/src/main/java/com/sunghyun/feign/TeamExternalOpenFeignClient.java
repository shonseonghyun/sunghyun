package com.sunghyun.feign;

import com.sunghyun.feign.dto.InternalTeamResponseDto;
import com.sunghyun.web.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "team-internal-api",
        url = "${team.api.url}",
        configuration = OpenFeignConfig.class
)
public interface TeamExternalOpenFeignClient {
    @GetMapping("/{teamNo}/summary")
    GlobalResponse<InternalTeamResponseDto> getTeamSimpleInfo(@PathVariable("teamNo") Long teamNo);
}
