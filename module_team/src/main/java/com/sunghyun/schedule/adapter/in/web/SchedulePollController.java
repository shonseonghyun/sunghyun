package com.sunghyun.schedule.adapter.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.schedule.application.dto.req.SchedulePollCreateReqDto;
import com.sunghyun.schedule.application.dto.req.SchedulePollLocationSetReqDto;
import com.sunghyun.schedule.application.dto.req.SchedulePollVoteReqDto;
import com.sunghyun.schedule.application.dto.res.MatchInfoResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollMinPlayerMetResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollResDto;
import com.sunghyun.schedule.application.dto.res.SchedulePollSimpleResDto;
import com.sunghyun.schedule.application.port.in.SchedulePollUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule-polls")
public class SchedulePollController {

    private final SchedulePollUseCase schedulePollUseCase;

    /**
     * 일정 투표 생성
     */
    @PostMapping
    public GlobalResponse createSchedulePoll(
            @AuthMember AuthMemberInfo authMemberInfo,
            @Valid @RequestBody SchedulePollCreateReqDto reqDto
    ) {
        Long memberNo = authMemberInfo.getMemberNo();
        schedulePollUseCase.createSchedulePoll(memberNo, reqDto);
        return GlobalResponse.of(ErrorCode.S000);
    }

    /**
     * 최소 인원 충족 일정 목록 조회 (팀 리더 전용)
     */
//    @GetMapping("/matchable")
//    public GlobalResponse getMinPlayerMetSchedulePolls(
//            @PathVariable Long teamNo,
//            @AuthMember AuthMemberInfo authMemberInfo
//    ) {
//        Long memberNo = authMemberInfo.getMemberNo();
//
//        List<SchedulePollMinPlayerMetResDto> result = schedulePollUseCase.getMinPlayerMetSchedulePolls(teamNo, memberNo);
//        return GlobalResponse.of(ErrorCode.S000, result);
//    }

    /**
     * 일정 투표 목록 조회 (No-Offset 페이징)
     */
    @GetMapping
    public GlobalResponse getSchedulePolls(
            @AuthMember AuthMemberInfo authMemberInfo,
            @RequestParam(required = false) Long lastSchedulePollNo,
            @RequestParam(defaultValue = "5") int size
    ) {
        Long memberNo = authMemberInfo.getMemberNo();

        List<SchedulePollSimpleResDto> result = schedulePollUseCase.getSchedulePolls(
                memberNo, lastSchedulePollNo, size
        );
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/{schedulePollNo}")
    public GlobalResponse getSchedulePollDetail(
            @PathVariable Long schedulePollNo,
            @AuthMember AuthMemberInfo authMemberInfo
    ) {
        Long memberNo = authMemberInfo.getMemberNo();

        SchedulePollResDto result = schedulePollUseCase.getSchedulePoll(schedulePollNo, memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    /**
     * 일정 참석/불참/미정 투표 실행
     */
    @PutMapping("/{schedulePollNo}/vote")
    public GlobalResponse vote(
            @PathVariable Long schedulePollNo,
            @AuthMember AuthMemberInfo authMemberInfo,
            @Valid @RequestBody SchedulePollVoteReqDto reqDto
    ) {
        Long memberNo = authMemberInfo.getMemberNo();

        schedulePollUseCase.vote(memberNo, schedulePollNo, reqDto.getStatus());
        return GlobalResponse.of(ErrorCode.S000);
    }

    @PutMapping("/{schedulePollNo}/location")
    public GlobalResponse setLocation(
            @PathVariable Long schedulePollNo,
            @AuthMember AuthMemberInfo authMemberInfo,
            @Valid @RequestBody SchedulePollLocationSetReqDto reqDto
    ){
        Long memberNo = authMemberInfo.getMemberNo();

        schedulePollUseCase.setLocation(memberNo,schedulePollNo,reqDto);
        return GlobalResponse.of(ErrorCode.S000);
    }
}