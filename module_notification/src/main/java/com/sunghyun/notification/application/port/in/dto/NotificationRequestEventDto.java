package com.sunghyun.notification.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestEventDto {
    private Long memberNo;
    private String email;
    private String subject;
    private String content;
}
