package com.sunghyun.member.application.port.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<Object> getRefreshToken(String id);
    void lock(String id, String refreshToken);
}
