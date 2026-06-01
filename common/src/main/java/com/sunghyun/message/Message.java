package com.sunghyun.message;

public interface Message<T> {
    String getSubject(T data);
    String getContent(T data);
}
