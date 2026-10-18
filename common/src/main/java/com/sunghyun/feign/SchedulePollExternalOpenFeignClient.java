package com.sunghyun.feign;

import com.sunghyun.feign.dto.InternalSchedulePollConflictResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.web.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "schedulePoll-internal-api",
        url = "${schedulePoll.api.url}",
        configuration = OpenFeignConfig.class
)
public interface SchedulePollExternalOpenFeignClient {

    @GetMapping("/{schedulePollNo}")
    GlobalResponse<InternalSchedulePollResponseDto> getSchedulePollSimpleInfo(
            @PathVariable("schedulePollNo") Long schedulePollNo
    );

    @GetMapping("/members/{memberNo}/conflicts")
    GlobalResponse<InternalSchedulePollConflictResponseDto> hasConflictingSchedulePoll(
            @PathVariable("memberNo") Long memberNo,
            @RequestParam("playDt") String playDt,
            @RequestParam("playTm") String playTm
    );
}