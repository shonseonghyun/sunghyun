package com.sunghyun.member.infrastructure.handler;

import com.sunghyun.member.domain.handler.RefreshTokenHandler;
import com.sunghyun.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenHandler implements RefreshTokenHandler {
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
    public Object getRefreshToken(String id) {
        final String key = getKey(id);
        return redisService.getValueByKey(key);
    }

    private String getKey(String id){
        return refreshTokenIdPrefix+id;
    }
}
