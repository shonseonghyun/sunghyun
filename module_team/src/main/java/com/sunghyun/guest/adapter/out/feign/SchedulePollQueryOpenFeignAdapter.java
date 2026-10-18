package com.sunghyun.guest.adapter.out.feign;

import com.sunghyun.feign.SchedulePollExternalOpenFeignClient;
import com.sunghyun.feign.dto.InternalSchedulePollConflictResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.guest.application.port.out.SchedulePollPort;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulePollQueryOpenFeignAdapter implements SchedulePollPort {
    private final SchedulePollExternalOpenFeignClient feignClient;

    @Override
    public GlobalResponse<InternalSchedulePollResponseDto> getSchedulePollSimpleInfo(Long schedulePollNo) {
        return feignClient.getSchedulePollSimpleInfo(schedulePollNo);
    }

    @Override
    public GlobalResponse<InternalSchedulePollConflictResponseDto> existsConflictingSchedulePoll(Long memberNo, String playDt, String playTm) {
        return feignClient.hasConflictingSchedulePoll(memberNo,playDt,playTm);
    }
}
