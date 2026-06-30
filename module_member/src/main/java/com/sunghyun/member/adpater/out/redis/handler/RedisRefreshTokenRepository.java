package com.sunghyun.member.adpater.out.redis.handler;

import com.sunghyun.member.application.port.repository.RefreshTokenRepository;
import com.sunghyun.redis.AbstractRedisRepository;
import com.sunghyun.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisRefreshTokenRepository extends AbstractRedisRepository implements RefreshTokenRepository {

    @Value("${member.refresh.token.prefix}")
    private String refreshTokenIdPrefix;

    @Value("${member.refresh.token.timeout}")
    private Long timeout;

    public RedisRefreshTokenRepository(RedisService redisService) {
        super(redisService);
    }

    @Override
    public void lock(String id,String refreshToken) {
        final String key = getKey(id);
        redisService.set(key,refreshToken,timeout);
    }

    @Override
    public Optional<Object> getRefreshToken(String id) {
        final String key = getKey(id);
        return Optional.ofNullable(getValue(key));
    }

    private String getKey(String id){
        return refreshTokenIdPrefix+id;
    }
}
