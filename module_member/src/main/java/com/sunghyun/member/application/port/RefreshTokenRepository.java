package com.sunghyun.member.application.port;

public interface RefreshTokenRepository {
    void lock(String id, String refreshToken);
    Object getRefreshToken(String id);
}
