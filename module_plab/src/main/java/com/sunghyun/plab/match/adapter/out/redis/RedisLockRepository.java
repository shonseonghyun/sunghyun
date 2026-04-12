package com.sunghyun.plab.match.adapter.out.redis;

import com.sunghyun.plab.match.application.port.out.repository.LockRepository;
import com.sunghyun.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockRepository implements LockRepository {
    private final RedisService redisService;

    @Override
    public boolean getLock(Long plabMatchNo) {
        final String key = plabMatchNo.toString();
        final String value = "registerPlabMatch";
        return redisService.setNX(key,value,10L);
    }

    @Override
    public void unlock(Long plabMatchNo) {
        final String key = plabMatchNo.toString();
        redisService.delete(key);
    }
}
