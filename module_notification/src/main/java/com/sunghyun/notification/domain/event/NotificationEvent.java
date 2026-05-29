package com.sunghyun.notification.domain.event;

import com.sunghyun.notification.config.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationEvent<T> {
    private Long memberNo;
    private String email;
    private Message<T> message;
    private T data;
}
