package com.sunghyun.guest.application.port.out;

import com.sunghyun.feign.dto.InternalSchedulePollConflictResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.web.GlobalResponse;

public interface SchedulePollPort {
    GlobalResponse<InternalSchedulePollResponseDto> getSchedulePollSimpleInfo(Long schedulePollNo);
    GlobalResponse<InternalSchedulePollConflictResponseDto> existsConflictingSchedulePoll(Long memberNo, String playDt, String playTm);
}
