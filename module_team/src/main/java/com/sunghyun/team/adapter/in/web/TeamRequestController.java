package com.sunghyun.team.adapter.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.team.application.port.in.TeamRequestUseCase;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/teams")
@RestController
@RequiredArgsConstructor
public class TeamRequestController {
    private final TeamRequestUseCase teamRequestUseCase;

    @PostMapping("/{teamNo}/requst")
    public GlobalResponse requestTeamJoin(@PathVariable Long teamNo, @AuthMember AuthMemberInfo authMemberInfo){
        teamRequestUseCase.requestTeamJoin(teamNo,authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000);
    }

    @PostMapping("/{requestNo}/accpet")
    public GlobalResponse acceptJoinRequest(@PathVariable Long requestNo, @AuthMember AuthMemberInfo authMemberInfo){
        teamRequestUseCase.acceptRequest(requestNo,authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000);
    }

    @PostMapping("/{requestNo}/reject")
    public GlobalResponse rejectJoinRequest(@PathVariable Long requestNo, @AuthMember AuthMemberInfo authMemberInfo){
        teamRequestUseCase.rejectRequest(requestNo,authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000);
    }

    @PostMapping("/{requestNo}/cancel")
    public GlobalResponse cancelJoinRequest(@PathVariable Long requestNo, @AuthMember AuthMemberInfo authMemberInfo){
        teamRequestUseCase.cancelRequest(requestNo,authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000);
    }
}
