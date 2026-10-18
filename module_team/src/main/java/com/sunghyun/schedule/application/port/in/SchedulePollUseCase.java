package com.sunghyun.schedule.application.port.in;

import com.sunghyun.feign.dto.InternalSchedulePollConflictResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.schedule.application.dto.req.SchedulePollCreateReqDto;
import com.sunghyun.schedule.application.dto.req.SchedulePollLocationSetReqDto;
import com.sunghyun.schedule.application.dto.res.MatchInfoResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollMinPlayerMetResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollSimpleResDto;
import com.sunghyun.schedule.domain.Location;
import com.sunghyun.schedule.domain.enums.AttendanceStatus;

import java.util.List;

public interface SchedulePollUseCase {
    void createSchedulePoll(Long memberNo, SchedulePollCreateReqDto reqDto);
    List<SchedulePollSimpleResDto> getSchedulePolls(Long memberNo, Long lastSchedulePollNo, int size);
    SchedulePollResDto getSchedulePoll(Long schedulePollNo, Long memberNo);
    void vote(Long memberNo, Long schedulePollNo, AttendanceStatus status);
    List<SchedulePollMinPlayerMetResDto> getMinPlayerMetSchedulePolls(Long teamNo, Long memberNo);
    InternalSchedulePollResponseDto getSchedulePollForServer(Long schedulePollNo);
    InternalSchedulePollConflictResponseDto hasConflictingSchedulePoll(Long memberNo, String playDt, String playTm);
    void setLocation(Long memberNo, Long schedulePollNo, SchedulePollLocationSetReqDto reqDto);
}
