package com.sunghyun.mail;

public interface MailMessage<T> {
    String getSubject(T data);
    String getContent(T data);
}
