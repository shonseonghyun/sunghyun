package com.sunghyun.plab.subscription.application.port.out.external;

import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;

public interface NotificationEventOutPort {
    void publish(final NotificationRequestedEvent requestedEvent);
}
