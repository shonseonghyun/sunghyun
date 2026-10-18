package com.sunghyun.team.adapter.in.web;

import com.sunghyun.team.application.port.in.TeamUseCase;
import com.sunghyun.team.application.port.out.dto.TeamSimpleInfoSelectResDto;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/internal/v1/teams")
@RestController
@RequiredArgsConstructor
public class InternalTeamController {
    private final TeamUseCase teamUseCase;

    @GetMapping("/{teamNo}/summary")
    public GlobalResponse getTeamSimpleInfo(@PathVariable Long teamNo){
        TeamSimpleInfoSelectResDto result = teamUseCase.getTeamSimpleInfo(teamNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}
