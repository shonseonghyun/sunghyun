package com.sunghyun.schedule.adapter.out.api;

import com.sunghyun.feign.GuestRecruitmentInternalOpenFeignClient;
import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.schedule.application.port.out.GuestQueryPort;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestRecruitmentOpenfeignAdapter implements GuestQueryPort {
    private final GuestRecruitmentInternalOpenFeignClient feignClient;

    @Override
    public GlobalResponse<InternalGuestRecruitmentDto> getGuestRecruitment(Long schedulePollNo) {
        return feignClient.getGuestRecruitmentSimpleInfo(schedulePollNo);
    }
}
