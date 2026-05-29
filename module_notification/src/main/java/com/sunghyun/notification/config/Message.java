package com.sunghyun.notification.config;

public interface Message<T> {
    String getSubject(T data);
    String getContent(T data);
}
