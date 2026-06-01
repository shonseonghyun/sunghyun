package com.sunghyun.notification.application.port.in.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRequestEventDto {
    private Long memberNo;
    private String email;
    private String subject;
    private String content;
}
