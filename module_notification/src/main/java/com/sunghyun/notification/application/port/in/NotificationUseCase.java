package com.sunghyun.notification.application.port.in;

import com.sunghyun.message.Message;
import com.sunghyun.notification.application.port.in.dto.NotificationRequestEventDto;

public interface NotificationUseCase {
    <T> void doNoti(final Long memberNo, final String email, Message<T> message, T item);
    void doNoti(final NotificationRequestEventDto dto);
}
