package com.sunghyun.notification.application.port.in;

import com.sunghyun.notification.application.port.in.dto.NotificationRequestEventDto;

public interface NotificationUseCase {
    void doNoti(final NotificationRequestEventDto dto);
}
