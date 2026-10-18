package com.sunghyun.team.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamJoinRequestAcceptedEvent {
    private Long teamNo;
    private Long requesterMemberNo;
}
