package com.sunghyun.schedule.adapter.in.web.internal;

import com.sunghyun.feign.dto.InternalSchedulePollConflictResponseDto;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.schedule.application.port.in.SchedulePollUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/schedule-polls")
public class InternalSchedulePollController {

    private final SchedulePollUseCase schedulePollUseCase;

    /**
     * 타 서버 연동용: 스케줄 투표 기반 경기 및 팀 상세 정보 조회
     */
    @GetMapping("/{schedulePollNo}")
    public GlobalResponse<InternalSchedulePollResponseDto> getSchedulePollForServer(
            @PathVariable Long schedulePollNo
    ) {
        InternalSchedulePollResponseDto result = schedulePollUseCase.getSchedulePollForServer(schedulePollNo);
        return GlobalResponse.of(ErrorCode.S000, result);
    }

    /**
     * 타 서버 연동용: 회원별 동일 시간대 일정 충돌 여부 조회
     */
    @GetMapping("/members/{memberNo}/conflicts")
    public GlobalResponse<InternalSchedulePollConflictResponseDto> hasConflictingSchedulePoll(
            @PathVariable Long memberNo,
            @RequestParam String playDt,
            @RequestParam String playTm
    ) {
        InternalSchedulePollConflictResponseDto result = schedulePollUseCase.hasConflictingSchedulePoll(memberNo, playDt, playTm);
        return GlobalResponse.of(ErrorCode.S000, result);
    }
}