package com.sunghyun.member.adpater.out.redis.handler;

import com.sunghyun.member.application.port.repository.RefreshTokenRepository;
import com.sunghyun.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {
    private final RedisService redisService;

    @Value("${member.refresh.token.prefix}")
    private String refreshTokenIdPrefix;

    @Value("${member.refresh.token.timeout}")
    private Long timeout;

    @Override
    public void lock(String id,String refreshToken) {
        final String key = getKey(id);
        redisService.set(key,refreshToken,timeout);
    }

    @Override
    public Optional<Object> getRefreshToken(String id) {
        final String key = getKey(id);
        return Optional.ofNullable(redisService.getValueByKey(key));
    }

    private String getKey(String id){
        return refreshTokenIdPrefix+id;
    }
}
