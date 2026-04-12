package com.sunghyun.member.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRegisteredEvent {
    private String key;
}
