package com.sunghyun.member.infrastructure.handler;

import com.sunghyun.member.domain.handler.MemberIdPendingHandler;
import com.sunghyun.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMemberIdPendingHandler implements MemberIdPendingHandler {
    private final RedisService redisService;

    @Override
    public boolean lock(
            final String key,
            final String value,
            final Long timeout
    )
    {
        return redisService.setNX(key, value, timeout);
    }

    @Override
    public void unlock(final String key) {
        redisService.delete(key);
    }

    @Override
    public Object getPendingValue(final String key) {
        return redisService.getValueByKey(key);
    }

    @Override
    public void deleteAllPendingIds(final String key) {
        redisService.deleteAllByKey(key);
    }

    @Override
    public void deletePendingId(final String key) {
        redisService.deleteByKey(key);
    }
}
