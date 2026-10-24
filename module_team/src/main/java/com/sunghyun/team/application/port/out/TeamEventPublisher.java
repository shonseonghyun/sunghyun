package com.sunghyun.team.application.port.out;

import com.sunghyun.event.team.TeamCreatedEvent;

public interface TeamEventPublisher {
    void publishTeamCreated(TeamCreatedEvent event);
}
