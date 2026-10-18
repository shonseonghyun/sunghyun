package com.sunghyun.team.adapter.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.team.application.port.in.TeamUseCase;
import com.sunghyun.team.application.port.in.dto.TeamCreateReqDto;
import com.sunghyun.team.application.port.out.dto.TeamCreateResDto;
import com.sunghyun.team.application.port.out.dto.TeamDetailInfoSelectResDto;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/api/v1/teams")
@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamUseCase teamUseCase;

    @PostMapping("")
    public GlobalResponse createTeam(@RequestBody TeamCreateReqDto reqDto, @AuthMember AuthMemberInfo authMemberInfo){
        TeamCreateResDto result = teamUseCase.createTeam(authMemberInfo.getMemberNo(),reqDto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/{teamNo}")
    public GlobalResponse getTeamDetailInfo(@PathVariable Long teamNo,@AuthMember AuthMemberInfo authMemberInfo){
        Long memberNo = (authMemberInfo != null) ? authMemberInfo.getMemberNo() : null;
        TeamDetailInfoSelectResDto result = teamUseCase.getTeamDetailInfo(teamNo,memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

//    @GetMapping("/{teamNo}/summary")
//    public GlobalResponse getTeamSimpleInfo(@PathVariable Long teamNo){
//        TeamSimpleInfoSelectResDto result = teamUseCase.getTeamSimpleInfo(teamNo);
//        return GlobalResponse.of(ErrorCode.S000,result);
//    }

    @GetMapping("/my")
    public GlobalResponse getMyTeamDetailInfo(@AuthMember AuthMemberInfo authMemberInfo){
        Long memberNo = authMemberInfo.getMemberNo();
        TeamDetailInfoSelectResDto result = teamUseCase.getTeamDetailInfoByMemberNo(memberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}
