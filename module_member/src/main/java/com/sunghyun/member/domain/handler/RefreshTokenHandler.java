package com.sunghyun.member.domain.handler;

public interface RefreshTokenHandler {
    void lock(String id, String refreshToken);
    Object getRefreshToken(String id);
}
