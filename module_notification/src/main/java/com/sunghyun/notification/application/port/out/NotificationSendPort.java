package com.sunghyun.notification.application.port.out;

import com.sunghyun.message.Message;

public interface NotificationSendPort {
    <T> void send(final String to, final Message<T> messageStrategy, final T data);
//    <T> MimeMessage createMessage(final String to, final Message<T> messageStrategy, final T data) throws MessagingException;

    void send(final String to,final String subject,final String content);
//    MimeMessage createMessage(final String to, final String subject, final String content) throws MessagingException;

}
