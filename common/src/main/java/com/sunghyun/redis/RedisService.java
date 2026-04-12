package com.sunghyun.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;

    public boolean setNX(final String key, final String value,final Long timeout){
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue()
                        // setIfAbsent 는 값이 없을 때만 저장하므로 선점 로직에 적합 (5분 유효)
                        .setIfAbsent(
                            key,
                            value,
                            Duration.ofSeconds(timeout)
                ));
    }

    public void delete(final String key){
        redisTemplate.delete(key);
    }

    public Object getValueByKey(final String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteByKey(final String key){
        redisTemplate.delete(key);
    }

    public void deleteAllByKey(final String key){
        Set<String> keys = redisTemplate.keys(key);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
