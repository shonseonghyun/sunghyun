package com.sunghyun.schedule.application.dto.req;

import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SchedulePollVoteReqDto {

    @NotNull(message = "투표 상태는 필수입니다.")
    private AttendanceStatus status;
}
