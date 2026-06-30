package com.sunghyun.member.adpater.out.redis.handler;

import com.sunghyun.member.application.port.repository.MemberIdPendingRepository;
import com.sunghyun.redis.AbstractRedisRepository;
import com.sunghyun.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisMemberIdPendingRepository extends AbstractRedisRepository implements MemberIdPendingRepository {

    @Value("${member.valid-id.prefix}")
    private String pendingIdPrefix;

    @Value("${member.valid-id.timeout}")
    private Long timeout;

    public RedisMemberIdPendingRepository(RedisService redisService) {
        super(redisService);
    }

    @Override
    public boolean lock(
            final String id,
            final String value
    )
    {
        String key = getKey(id);
        return this.redisService.setNX(key, value, timeout);
    }

    @Override
    public void unlock(final String id) {
        String key = getKey(id);
        delete(key);
    }

    @Override
    public Optional<Object> getPendingToken(final String id) {
        String key = getKey(id);
        return getValue(key);
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
