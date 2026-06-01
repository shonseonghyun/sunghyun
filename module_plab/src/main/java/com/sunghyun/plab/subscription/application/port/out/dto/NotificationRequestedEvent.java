package com.sunghyun.plab.subscription.application.port.out.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequestedEvent {
    private Long memberNo;
    private String email;
    private String subject;
    private String content;
}
