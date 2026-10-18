package com.sunghyun.team.adapter.in.listener;

import com.sunghyun.team.application.port.in.TeamUseCase;
import com.sunghyun.team.domain.event.TeamJoinRequestAcceptedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamListener {
    private final TeamUseCase teamUseCase;

    @EventListener
    public void handle(TeamJoinRequestAcceptedEvent event){
        log.info("팀 수락에 대한 후처리 진행");
        Long teamNo = event.getTeamNo();
        Long requesterMemberNo = event.getRequesterMemberNo();
        teamUseCase.acceptMember(teamNo,requesterMemberNo);
    }
}
