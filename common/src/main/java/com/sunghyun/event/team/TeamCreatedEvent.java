package com.sunghyun.event.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreatedEvent {
    private Long teamNo;
    private Long creatorMemberNo;
}
