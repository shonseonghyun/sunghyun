package com.sunghyun.member.application.port.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
    void lock(String id, String refreshToken);
    Optional<Object> getRefreshToken(String id);
}
