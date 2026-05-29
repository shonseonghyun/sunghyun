package com.sunghyun.notification.application.port.out;

import com.sunghyun.notification.config.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface NotificationSendPort {
    <T> void send(final String to, final Message<T> messageStrategy, final T data);
    <T> MimeMessage createMessage(final String to, final Message<T> messageStrategy, final T data) throws MessagingException;
}
