package com.sunghyun.schedule.application.port.out;

import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.web.GlobalResponse;

public interface GuestQueryPort {
    GlobalResponse<InternalGuestRecruitmentDto> getGuestRecruitment(Long recruitmentNo);
}
