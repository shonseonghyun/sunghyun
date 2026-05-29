package com.sunghyun.notification.application.port.in;

import com.sunghyun.notification.config.Message;

public interface NotificationUseCase {
    <T> void doNoti(final Long memberNo, final String email, Message<T> message, T item);
}
