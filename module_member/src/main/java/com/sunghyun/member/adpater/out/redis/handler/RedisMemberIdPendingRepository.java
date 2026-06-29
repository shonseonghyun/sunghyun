package com.sunghyun.member.adpater.out.redis.handler;

import com.sunghyun.member.application.port.MemberIdPendingRepository;
import com.sunghyun.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMemberIdPendingRepository implements MemberIdPendingRepository {
    private final RedisService redisService;

    @Value("${member.valid-id.prefix}")
    private String pendingIdPrefix;

    @Value("${member.valid-id.timeout}")
    private Long timeout;

    @Override
    public boolean lock(
            final String id,
            final String value
    )
    {
        String key = getKey(id);
        return redisService.setNX(key, value, timeout);
    }

    @Override
    public void unlock(final String id) {
        String key = getKey(id);
        redisService.delete(key);
    }

    @Override
    public Object getPendingValue(final String id) {
        String key = getKey(id);
        return redisService.getValueByKey(key);
    }

    @Override
    public void deleteAllPendingIds(final String id) {
        String key = getKey(id);
        redisService.deleteAllByKey(key);
    }

    @Override
    public void deletePendingId(final String id) {
        String key = getKey(id);
        redisService.deleteByKey(key);
    }

    private String getKey(String id){
        return pendingIdPrefix+id;
    }
}
